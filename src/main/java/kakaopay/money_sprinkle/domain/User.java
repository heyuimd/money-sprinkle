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
public class User extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
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
        // 단위: 천원
        user.money = 999999999999999L;

        return user;
    }

    public void addMoney(int money) {
        // 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않는다.
        this.money += money;
    }

    public void removeMoney(int money) {
        // 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않는다.
        this.money -= money;
    }

    public boolean isUserIn(Room room) {
        return roomInOutList.stream()
                .anyMatch(o -> o.getRoom() == room && o.getStatus() == RoomInOutStatus.IN);
    }
}
