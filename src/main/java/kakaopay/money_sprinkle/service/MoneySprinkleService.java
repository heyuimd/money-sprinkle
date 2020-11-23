package kakaopay.money_sprinkle.service;

import kakaopay.money_sprinkle.domain.MoneySprinkle;
import kakaopay.money_sprinkle.domain.Room;
import kakaopay.money_sprinkle.domain.User;
import kakaopay.money_sprinkle.exception.MyEntityNotFoundException;
import kakaopay.money_sprinkle.exception.TimePassedException;
import kakaopay.money_sprinkle.repository.MoneySprinkleRepository;
import kakaopay.money_sprinkle.repository.RoomRepository;
import kakaopay.money_sprinkle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MoneySprinkleService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MoneySprinkleRepository moneySprinkleRepository;

    @Transactional
    public String sprinkleMoney(Long userId, Long roomId, int money, int count) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new MyEntityNotFoundException("사용자를 찾을 수 없습니다."));

        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new MyEntityNotFoundException("방을 찾을 수 없습니다."));

        MoneySprinkle moneySprinkle = MoneySprinkle.sprinkleMoney(user, room, money, count);
        moneySprinkleRepository.save(moneySprinkle);

        return moneySprinkle.getToken();
    }

    @Transactional
    public void pickUpMoney(String token, Long userId) {
        User user = userRepository.findByIdWithRoomInOut(userId).orElseThrow(
                () -> new MyEntityNotFoundException("사용자를 찾을 수 없습니다."));

        MoneySprinkle moneySprinkle = moneySprinkleRepository.findByToken(token).orElseThrow(
                () -> new MyEntityNotFoundException("요청한 뿌리기 정보를 찾을 수 없습니다."));

        moneySprinkle.pickUpMoney(user);
    }

    public MoneySprinkle findByTokenAndUserId(String token, Long userId) {
        MoneySprinkle moneySprinkle = moneySprinkleRepository.findByTokenAndUserId(token, userId).orElseThrow(
                () -> new MyEntityNotFoundException("요청한 뿌리기 정보를 찾을 수 없습니다."));

        if (moneySprinkle.getCreatedAt().plusDays(7).isBefore(LocalDateTime.now())) {
            throw new TimePassedException("뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.");
        }

        return moneySprinkle;
    }
}
