package kakaopay.money_sprinkle.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomInOut extends BaseDateTime {

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
    private RoomInOutStatus status;

    /**
     * 사용자가 방에 최초 입장
     *
     * @param user 입장하는 사용자
     * @param room 입장할 방
     * @return 생성된 userRoom
     */
    public static RoomInOut enterRoom(User user, Room room) {
        RoomInOut roomInOut = new RoomInOut();
        roomInOut.user = user;
        roomInOut.room = room;
        roomInOut.status = RoomInOutStatus.IN;

        user.getRoomInOutList().add(roomInOut);
        room.getRoomInOutList().add(roomInOut);

        return roomInOut;
    }

    /**
     * 사용자가 방에 재입장
     */
    public void reenterRoom() {
        if (status == RoomInOutStatus.OUT) {
            status = RoomInOutStatus.IN;
        }
    }

    /**
     * 사용자가 방에서 퇴장
     */
    public void leaveRoom() {
        if (status == RoomInOutStatus.IN) {
            status = RoomInOutStatus.OUT;
        }
    }
}
