package kakaopay.money_sprinkle.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    private String loginId;

    private Long money;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRoom> userRooms = new ArrayList<>();

    public User(String loginId) {
        this.loginId = loginId;

        // 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정한다.
        this.money = 999999999999999L;
    }
}
