package kakaopay.money_sprinkle.repository;

import kakaopay.money_sprinkle.domain.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
}
