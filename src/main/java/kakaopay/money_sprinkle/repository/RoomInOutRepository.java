package kakaopay.money_sprinkle.repository;

import kakaopay.money_sprinkle.domain.Room;
import kakaopay.money_sprinkle.domain.RoomInOut;
import kakaopay.money_sprinkle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomInOutRepository extends JpaRepository<RoomInOut, Long> {

    Optional<RoomInOut> findByUserAndRoom(User user, Room room);
}
