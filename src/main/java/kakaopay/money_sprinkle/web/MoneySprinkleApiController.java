package kakaopay.money_sprinkle.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MoneySprinkleApiController {

    @PostMapping("/api/money-sprinkle")
    public MoneySprinkleResponseDto createMoneySprinkle(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") Long roomId,
            @RequestBody MoneySprinkleRequestDto request
    ) {
        return new MoneySprinkleResponseDto("123");
    }

    @PostMapping("/api/money-sprinkle/{token}/pick-up")
    public PickUpResponseDto pickUpMoney(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") Long roomId,
            @PathVariable("token") String token
    ) {
        return new PickUpResponseDto(0L);
    }

    @GetMapping("/api/money-sprinkle/{token}")
    public GetMoneySprinkleResponseDto getMoneySprinkle(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") Long roomId,
            @PathVariable("token") String token
    ) {
        return new GetMoneySprinkleResponseDto(0L, 0L, LocalDateTime.now(), null);
    }

    @Data
    private class MoneySprinkleRequestDto {
        private Long money;
        private Long count;
    }

    @Data
    @AllArgsConstructor
    private class MoneySprinkleResponseDto {
        private String token;
    }

    @Data
    @AllArgsConstructor
    private class PickUpResponseDto {
        private Long money;
    }

    @Data
    @AllArgsConstructor
    private class SprinkledMoneyDto {
        private Long pickedBy;
        private Long money;
    }

    @Data
    @AllArgsConstructor
    private class GetMoneySprinkleResponseDto {
        private Long totalMoney;
        private Long pickedUpMoney;
        private LocalDateTime sprinkledAt;
        private List<SprinkledMoneyDto> detail;
    }
}
