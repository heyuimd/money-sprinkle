package kakaopay.money_sprinkle.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class User extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    private String loginId;

    private Long money;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRoom> userRooms = new ArrayList<>();
}
