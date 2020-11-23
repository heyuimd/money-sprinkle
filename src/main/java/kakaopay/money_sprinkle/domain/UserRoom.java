package kakaopay.money_sprinkle.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRoom extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID")
    private Room room;

    @Enumerated(EnumType.STRING)
    private UserRoomStatus status;

    /**
     * 사용자가 방에 최초 입장
     *
     * @param user 입장하는 사용자
     * @param room 입장할 방
     * @return 생성된 userRoom
     */
    public static UserRoom enterRoom(User user, Room room) {
        UserRoom userRoom = new UserRoom();
        userRoom.user = user;
        userRoom.room = room;
        userRoom.status = UserRoomStatus.ENTERED;

        return userRoom;
    }

    /**
     * 사용자가 방에 재입장
     */
    public void reenterRoom() {
        if (status == UserRoomStatus.LEFT) {
            status = UserRoomStatus.ENTERED;
        }
    }

    /**
     * 사용자가 방에서 퇴장
     */
    public void leaveRoom() {
        if (status == UserRoomStatus.ENTERED) {
            status = UserRoomStatus.LEFT;
        }
    }
}
