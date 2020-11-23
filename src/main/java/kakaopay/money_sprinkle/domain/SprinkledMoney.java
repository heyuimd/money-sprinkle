package kakaopay.money_sprinkle.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SprinkledMoney extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    private Integer money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PICKED_UP_BY")
    private User pickedUpBy;

    private LocalDateTime pickedUpAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPRINKLE_ID")
    private MoneySprinkle sprinkle;

    public SprinkledMoney(Integer money, MoneySprinkle sprinkle) {
        this.money = money;
        this.sprinkle = sprinkle;
    }
}
