package kakaopay.money_sprinkle.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Room extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<UserRoom> userRooms = new ArrayList<>();
}
