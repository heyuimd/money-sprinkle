package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.exception.NoMoreSplitsException;
import kakaopay.money_sprinkle.exception.NotEnoughToSplitException;
import kakaopay.money_sprinkle.exception.NotPickableUserException;
import kakaopay.money_sprinkle.exception.TimePassedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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
        if (money < count) {
            throw new NotEnoughToSplitException("사용자가 최소 천원씩은 받아 갈 수 있어야 합니다.");
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

    public SprinkledMoney pickUpMoney(User user) {
        if (this.user == user) {
            throw new NotPickableUserException("돈을 뿌린 사용자는 받아갈 수 없습니다.");
        }

        if (getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new TimePassedException("돈 뿌린 후, 10분 후는 받아갈 수 없습니다.");
        }

        if (!user.isUserIn(room)) {
            throw new NotPickableUserException("같은 방에 뿌려진 돈만 가져갈 수 있습니다.");
        }

        sprinkledMoneyList.stream()
                .filter(o -> o.getPickedUpBy() == user)
                .findFirst()
                .ifPresent(o -> {
                    throw new NotPickableUserException("이미 받아간 뿌리기 입니다.");
                });

        SprinkledMoney sprinkledMoney = sprinkledMoneyList.stream()
                .filter(o -> o.getPickedUpBy() == null)
                .findFirst()
                .orElseThrow(() -> new NoMoreSplitsException("받아 갈 수 있는 돈이 없습니다."));

        sprinkledMoney.pickUp(user);

        return sprinkledMoney;
    }
}
