package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.repository.RoomInOutRepository;
import kakaopay.money_sprinkle.repository.RoomRepository;
import kakaopay.money_sprinkle.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomInOutTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomInOutRepository roomInOutRepository;

    @Test
    public void test_방에_입장() throws Exception {
        // given
        User user = User.createUser("user1", "user1");
        userRepository.save(user);
        Room room = Room.createRoom(user, "room1");
        roomRepository.save(room);

        // when
        roomInOutRepository.save(RoomInOut.enterRoom(user, room));
        RoomInOut roomInOut = roomInOutRepository
                .findByUserAndRoom(user, room).orElseThrow();

        // then
        assertThat(roomInOut.getStatus()).isEqualTo(RoomInOutStatus.IN);
    }

    @Test
    public void test_방에서_퇴장() throws Exception {
        // given
        User user1 = User.createUser("user1", "user1");
        userRepository.save(user1);
        User user2 = User.createUser("user2", "user2");
        userRepository.save(user2);
        Room room = Room.createRoom(user1, "room1");
        roomRepository.save(room);

        // when
        // 두명 입장
        roomInOutRepository.save(RoomInOut.enterRoom(user1, room));
        roomInOutRepository.save(RoomInOut.enterRoom(user2, room));
        // 한명 퇴장
        RoomInOut roomInOut = roomInOutRepository
                .findByUserAndRoom(user1, room).orElseThrow();
        roomInOut.leaveRoom();

        // then
        List<RoomInOut> roomInOutList = roomInOutRepository.findAll();
        long count = roomInOutList.stream()
                .filter(o -> o.getStatus() == RoomInOutStatus.IN)
                .count();
        assertThat(count).isEqualTo(1);
    }
}