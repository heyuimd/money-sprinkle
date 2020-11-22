package kakaopay.money_sprinkle.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
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
}
