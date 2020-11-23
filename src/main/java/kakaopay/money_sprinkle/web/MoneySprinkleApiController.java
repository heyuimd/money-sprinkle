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
        return new PickUpResponseDto(0);
    }

    @GetMapping("/api/money-sprinkle/{token}")
    public GetMoneySprinkleResponseDto getMoneySprinkle(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") Long roomId,
            @PathVariable("token") String token
    ) {
        return new GetMoneySprinkleResponseDto(0, 0, LocalDateTime.now(), null);
    }

    @Data
    private class MoneySprinkleRequestDto {
        private Integer money;
        private Integer count;
    }

    @Data
    @AllArgsConstructor
    private class MoneySprinkleResponseDto {
        private String token;
    }

    @Data
    @AllArgsConstructor
    private class PickUpResponseDto {
        private Integer money;
    }

    @Data
    @AllArgsConstructor
    private class SprinkledMoneyDto {
        private Integer pickedBy;
        private Integer money;
    }

    @Data
    @AllArgsConstructor
    private class GetMoneySprinkleResponseDto {
        private Integer totalMoney;
        private Integer pickedUpMoney;
        private LocalDateTime sprinkledAt;
        private List<SprinkledMoneyDto> detail;
    }
}
