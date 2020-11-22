package kakaopay.money_sprinkle.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class SprinkledMoney extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    private Long money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PICKED_UP_BY")
    private User pickedUpBy;

    private LocalDateTime pickedUpAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPRINKLE_ID")
    private MoneySprinkle sprinkle;
}
