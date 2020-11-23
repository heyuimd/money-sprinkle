package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.exception.NoMoreSplitsException;
import kakaopay.money_sprinkle.exception.NotEnoughToSplitException;
import kakaopay.money_sprinkle.exception.NotPickableUserException;
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
        assertThat(moneySprinkleFound.getSprinkledMoneyList().stream()
                .filter(o -> o.getMoney() > 0).count()
        ).isEqualTo(numToSplit);
        // 분배된 총합은 뿌린 돈과 같아야 한다.
        assertThat(moneySprinkleFound.getSprinkledMoneyList().stream()
                .map(SprinkledMoney::getMoney).reduce(0, Integer::sum)
        ).isEqualTo(moneyToSprinkle);
    }

    @Test
    public void test_돈_뿌리기__실패() throws Exception {
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

    @Test
    public void test_돈_가져가기() throws Exception {
        // given
        // 사용자 생성
        User user1 = User.createUser("user1", "user1");
        User user2 = User.createUser("user2", "user2");
        userRepository.save(user1);
        userRepository.save(user2);
        // 방 생성
        Room room = Room.createRoom(user1, "room1");
        roomRepository.save(room);
        // 사용자 방 입장
        roomInOutRepository.save(RoomInOut.enterRoom(user1, room));
        roomInOutRepository.save(RoomInOut.enterRoom(user2, room));
        // 돈 뿌리기
        int numToSplit = 10;
        MoneySprinkle moneySprinkle = MoneySprinkle.sprinkleMoney(user1, room, 100, numToSplit);
        moneySprinkleRepository.save(moneySprinkle);

        // when
        MoneySprinkle moneySprinkleFound = moneySprinkleRepository
                .findByToken(moneySprinkle.getToken()).orElseThrow();
        SprinkledMoney sprinkledMoney = moneySprinkleFound.pickUpMoney(user2);

        // then
        assertThat(sprinkledMoney.getPickedUpBy()).isEqualTo(user2);
        assertThat(sprinkledMoney.getPickedUpAt()).isNotNull();
        assertThat(moneySprinkleFound.getSprinkledMoneyList().stream()
                .filter(o -> o.getPickedUpBy() == null).count()
        ).isEqualTo(numToSplit - 1);
    }

    @Test
    public void test_돈_가져가기__본인이_뿌린_돈은_못_가져가기_때문에_실패() throws Exception {
        // given
        // 사용자 생성
        User user1 = User.createUser("user1", "user1");
        userRepository.save(user1);
        // 방 생성
        Room room = Room.createRoom(user1, "room1");
        roomRepository.save(room);
        // 사용자 방 입장
        roomInOutRepository.save(RoomInOut.enterRoom(user1, room));
        // 돈 뿌리기
        MoneySprinkle moneySprinkle = MoneySprinkle.sprinkleMoney(user1, room, 100, 10);
        moneySprinkleRepository.save(moneySprinkle);

        // when
        MoneySprinkle moneySprinkleFound = moneySprinkleRepository
                .findByToken(moneySprinkle.getToken()).orElseThrow();

        // then
        assertThatThrownBy(() -> {
            moneySprinkleFound.pickUpMoney(user1);
        }).isInstanceOf(NotPickableUserException.class)
                .hasMessageContaining("돈을 뿌린 사용자는 받아갈 수 없습니다.");
    }

    @Test
    public void test_돈_가져가기__더_이상_가져갈_돈이_없어서_실패() throws Exception {
        // given
        // 사용자 생성
        User user1 = User.createUser("user1", "user1");
        User user2 = User.createUser("user2", "user2");
        User user3 = User.createUser("user3", "user3");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        // 방 생성
        Room room = Room.createRoom(user1, "room1");
        roomRepository.save(room);
        // 사용자 방 입장
        roomInOutRepository.save(RoomInOut.enterRoom(user1, room));
        roomInOutRepository.save(RoomInOut.enterRoom(user2, room));
        roomInOutRepository.save(RoomInOut.enterRoom(user3, room));
        // 돈 뿌리기
        MoneySprinkle moneySprinkle = MoneySprinkle.sprinkleMoney(user1, room, 100, 1);
        moneySprinkleRepository.save(moneySprinkle);

        // when
        MoneySprinkle moneySprinkleFound = moneySprinkleRepository
                .findByToken(moneySprinkle.getToken()).orElseThrow();

        // then
        moneySprinkleFound.pickUpMoney(user2);
        assertThatThrownBy(() -> {
            moneySprinkleFound.pickUpMoney(user3);
        }).isInstanceOf(NoMoreSplitsException.class);
    }

    @Test
    public void test_돈_가져가기__한번_더_가져갈려고_해서_실패() throws Exception {
        // given
        // 사용자 생성
        User user1 = User.createUser("user1", "user1");
        User user2 = User.createUser("user2", "user2");
        userRepository.save(user1);
        userRepository.save(user2);
        // 방 생성
        Room room = Room.createRoom(user1, "room1");
        roomRepository.save(room);
        // 사용자 방 입장
        roomInOutRepository.save(RoomInOut.enterRoom(user1, room));
        roomInOutRepository.save(RoomInOut.enterRoom(user2, room));
        // 돈 뿌리기
        MoneySprinkle moneySprinkle = MoneySprinkle.sprinkleMoney(user1, room, 100, 1);
        moneySprinkleRepository.save(moneySprinkle);

        // when
        MoneySprinkle moneySprinkleFound = moneySprinkleRepository
                .findByToken(moneySprinkle.getToken()).orElseThrow();

        // then
        moneySprinkleFound.pickUpMoney(user2);
        assertThatThrownBy(() -> {
            moneySprinkleFound.pickUpMoney(user2);
        }).isInstanceOf(NotPickableUserException.class)
                .hasMessageContaining("이미 받아간 뿌리기 입니다.");
    }

    @Test
    public void test_돈_가져가기__방에_입장하지_않은_사용자가_가져갈려고_해서_실패() throws Exception {
        // given
        // 사용자 생성
        User user1 = User.createUser("user1", "user1");
        User user2 = User.createUser("user2", "user2");
        userRepository.save(user1);
        userRepository.save(user2);
        // 방 생성
        Room room1 = Room.createRoom(user1, "room1");
        roomRepository.save(room1);
        Room room2 = Room.createRoom(user2, "room2");
        roomRepository.save(room2);
        // 사용자 방 입장
        roomInOutRepository.save(RoomInOut.enterRoom(user1, room1));
        roomInOutRepository.save(RoomInOut.enterRoom(user2, room2));
        // 돈 뿌리기
        MoneySprinkle moneySprinkle = MoneySprinkle.sprinkleMoney(user1, room1, 100, 1);
        moneySprinkleRepository.save(moneySprinkle);

        // when
        MoneySprinkle moneySprinkleFound = moneySprinkleRepository
                .findByToken(moneySprinkle.getToken()).orElseThrow();

        // then
        assertThatThrownBy(() -> {
            moneySprinkleFound.pickUpMoney(user2);
        }).isInstanceOf(NotPickableUserException.class)
                .hasMessageContaining("같은 방에 뿌려진 돈만 가져갈 수 있습니다.");
    }
}