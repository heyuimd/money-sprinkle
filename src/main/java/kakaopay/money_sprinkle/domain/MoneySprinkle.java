package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.exception.NotEnoughToSplitException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private Integer money;

    private Integer count;

    @OneToMany(mappedBy = "sprinkle", cascade = CascadeType.ALL)
    private List<SprinkledMoney> sprinkledMoneyList = new ArrayList<>();

    private static String makeRandomToken(int size) {
        StringBuilder symbolBuilder = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ch++) {
            symbolBuilder.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            symbolBuilder.append(ch);
        }
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            symbolBuilder.append(ch);
        }
        char[] symbol = symbolBuilder.toString().toCharArray();
        Random random = new Random();

        char[] buffer = new char[size];
        for (int i = 0; i < size; i++) {
            buffer[i] = symbol[random.nextInt(symbol.length)];
        }
        return new String(buffer);
    }

    private static List<Integer> splitMoney(int money, int count) {
        // 최소 두당 천원씩은 돌아가야 한다.
        if (money < count) {
            throw new NotEnoughToSplitException();
        }

        // 랜덤하게 분배
        Random random = new Random();
        List<Integer> splits = new ArrayList<>();

        for (int i = 0; i < count - 1; i++) {
            int total = money - splits.stream().mapToInt(Integer::intValue).sum();
            int maxMoney = total / (count - splits.size());
            splits.add(Math.max(random.nextInt(maxMoney), 1));
        }

        // 마지막은 나머지 다 분배
        splits.add(money - splits.stream().mapToInt(Integer::intValue).sum());

        return splits;
    }

    public static MoneySprinkle sprinkleMoney(User user, Room room, int money, int count) {
        List<Integer> splits = splitMoney(money, count);

        MoneySprinkle moneySprinkle = new MoneySprinkle();
        moneySprinkle.token = makeRandomToken(3);
        moneySprinkle.user = user;
        moneySprinkle.room = room;
        moneySprinkle.money = money;
        moneySprinkle.count = count;

        for (Integer split : splits) {
            moneySprinkle.sprinkledMoneyList.add(new SprinkledMoney(split, moneySprinkle));
        }

        user.removeMoney(money);

        return moneySprinkle;
    }
}
