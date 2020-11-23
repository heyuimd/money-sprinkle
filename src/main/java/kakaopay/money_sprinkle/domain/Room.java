package kakaopay.money_sprinkle.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToMany(mappedBy = "room")
    private List<RoomInOut> roomInOutList = new ArrayList<>();

    /**
     * 방 생성
     *
     * @param createdBy 방을 생성한 사용자
     * @param roomName  방 이름
     * @return 생성된 방
     */
    public static Room createRoom(User createdBy, String roomName) {
        Room room = new Room();
        room.createdBy = createdBy;
        room.name = roomName;
        room.status = RoomStatus.OPEN;

        return room;
    }
}
