package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.repository.RoomRepository;
import kakaopay.money_sprinkle.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void test_방_생성() throws Exception {
        // given
        User user = User.createUser("user1", "홍길동");
        userRepository.save(user);
        String roomName = "room A";
        Room room = Room.createRoom(user, roomName);
        roomRepository.save(room);

        // when
        Room roomFound = roomRepository.findById(room.getId()).orElseThrow();

        // then
        assertThat(roomFound).isEqualTo(room);
        assertThat(roomFound.getCreatedBy()).isEqualTo(user);
        assertThat(roomFound.getName()).isEqualTo(roomName);
    }
}