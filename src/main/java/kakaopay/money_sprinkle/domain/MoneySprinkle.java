package kakaopay.money_sprinkle.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class MoneySprinkle extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID")
    private Room room;

    @OneToMany(mappedBy = "sprinkle", fetch = FetchType.LAZY)
    private List<SprinkledMoney> sprinkledMonies = new ArrayList<>();
}
