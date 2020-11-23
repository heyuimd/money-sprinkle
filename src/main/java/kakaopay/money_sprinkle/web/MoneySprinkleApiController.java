package kakaopay.money_sprinkle.web;

import kakaopay.money_sprinkle.domain.MoneySprinkle;
import kakaopay.money_sprinkle.dto.BadRequestDto;
import kakaopay.money_sprinkle.service.MoneySprinkleService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MoneySprinkleApiController {

    private final MoneySprinkleService moneySprinkleService;

    @PostMapping("/api/money-sprinkle")
    public ResponseEntity<?> createMoneySprinkle(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") Long roomId,
            @RequestBody MoneySprinkleRequestDto request
    ) {
        try {
            String token = moneySprinkleService.sprinkleMoney(
                    userId, roomId, request.getMoney(), request.getCount());
            return ResponseEntity.ok(new MoneySprinkleResponseDto(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BadRequestDto(e.getMessage()));
        }
    }

    @PostMapping("/api/money-sprinkle/{token}/pick-up")
    public ResponseEntity<?> pickUpMoney(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") Long roomId,
            @PathVariable("token") String token
    ) {
        try {
            moneySprinkleService.pickUpMoney(token, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BadRequestDto(e.getMessage()));
        }
    }

    @GetMapping("/api/money-sprinkle/{token}")
    public ResponseEntity<?> getMoneySprinkle(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") Long roomId,
            @PathVariable("token") String token
    ) {
        try {
            MoneySprinkle moneySprinkle = moneySprinkleService.findByTokenAndUserId(token, userId);
            List<SprinkledMoneyDto> sprinkledMoneyDtoList = moneySprinkle.getSprinkledMoneyList().stream()
                    .map(o -> new SprinkledMoneyDto(o.getPickedUpBy().getId(), o.getMoney()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new GetMoneySprinkleResponseDto(
                    moneySprinkle.getMoney(), moneySprinkle.getPickUpMoney(),
                    moneySprinkle.getCreatedAt(), sprinkledMoneyDtoList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BadRequestDto(e.getMessage()));
        }
    }

    @Data
    static class MoneySprinkleRequestDto {
        private Integer money;
        private Integer count;
    }

    @Data
    @AllArgsConstructor
    public static class MoneySprinkleResponseDto {
        private String token;
    }

    @Data
    @AllArgsConstructor
    public static class SprinkledMoneyDto {
        private Long pickedBy;
        private Integer money;
    }

    @Data
    @AllArgsConstructor
    public static class GetMoneySprinkleResponseDto {
        private Integer totalMoney;
        private Integer pickedUpMoney;
        private LocalDateTime sprinkledAt;
        private List<SprinkledMoneyDto> detail;
    }
}
