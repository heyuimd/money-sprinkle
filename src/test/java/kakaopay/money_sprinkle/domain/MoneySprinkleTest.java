package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.exception.NotEnoughToSplitException;
import kakaopay.money_sprinkle.repository.MoneySprinkleRepository;
import kakaopay.money_sprinkle.repository.RoomInOutRepository;
import kakaopay.money_sprinkle.repository.RoomRepository;
import kakaopay.money_sprinkle.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MoneySprinkleTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomInOutRepository roomInOutRepository;

    @Autowired
    private MoneySprinkleRepository moneySprinkleRepository;

    @Test
    public void test_돈_뿌리기() throws Exception {
        // given
        // 사용자 생성
        User user = User.createUser("user1", "user1");
        userRepository.save(user);
        // 방 생성
        Room room = Room.createRoom(user, "room1");
        roomRepository.save(room);
        // 사용자 방 입장
        roomInOutRepository.save(RoomInOut.enterRoom(user, room));

        // when
        int moneyToSprinkle = 100;
        int numToSplit = 10;
        Long userMoney = user.getMoney();
        MoneySprinkle moneySprinkle = MoneySprinkle.sprinkleMoney(user, room, moneyToSprinkle, numToSplit);
        moneySprinkleRepository.save(moneySprinkle);

        // then
        MoneySprinkle moneySprinkleFound = moneySprinkleRepository
                .findByToken(moneySprinkle.getToken()).orElseThrow();
        assertThat(user.getMoney()).isEqualTo(userMoney - moneyToSprinkle);
        assertThat(moneySprinkleFound).isEqualTo(moneySprinkle);
        assertThat(moneySprinkleFound.getUser()).isEqualTo(user);
        assertThat(moneySprinkleFound.getRoom()).isEqualTo(room);
        assertThat(moneySprinkleFound.getMoney()).isEqualTo(moneyToSprinkle);
        assertThat(moneySprinkleFound.getCount()).isEqualTo(numToSplit);
        assertThat(moneySprinkleFound.getSprinkledMoneyList().size()).isEqualTo(numToSplit);
        // 분배된 돈이 0 보다 커야 한다.
        assertThat(moneySprinkleFound.getSprinkledMoneyList().stream().filter(o -> o.getMoney() > 0).count())
                .isEqualTo(numToSplit);
    }

    @Test
    public void test_돈_뿌리기_실패() throws Exception {
        // given
        // 사용자 생성
        User user = User.createUser("user1", "user1");
        userRepository.save(user);
        // 방 생성
        Room room = Room.createRoom(user, "room1");
        roomRepository.save(room);
        // 사용자 방 입장
        roomInOutRepository.save(RoomInOut.enterRoom(user, room));

        // when
        int moneyToSprinkle = 9;
        int numToSplit = 10;

        // then
        assertThatThrownBy(() -> {
            MoneySprinkle.sprinkleMoney(user, room, moneyToSprinkle, numToSplit);
        }).isInstanceOf(NotEnoughToSplitException.class);
    }
}