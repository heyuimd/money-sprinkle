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

    private String name;

    private Long money;

    @OneToMany(mappedBy = "user")
    private List<RoomInOut> roomInOutList = new ArrayList<>();

    /**
     * 사용자 생성
     *
     * @param loginId  사용자 로그인 아이디
     * @param userName 사용자 이름
     * @return 생성된 사용자
     */
    public static User createUser(String loginId, String userName) {
        User user = new User();
        user.loginId = loginId;
        user.name = userName;

        // 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정한다.
        user.money = 999999999999999L;

        return user;
    }
}
