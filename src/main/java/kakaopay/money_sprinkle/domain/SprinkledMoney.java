package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.exception.AlreadyPickedUpException;
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

    public void pickUp(User pickedUpBy) {
        if (this.pickedUpBy != null) {
            throw new AlreadyPickedUpException("이미 가져간 사용자가 있습니다.");
        }

        this.pickedUpBy = pickedUpBy;
        pickedUpAt = LocalDateTime.now();
    }
}
