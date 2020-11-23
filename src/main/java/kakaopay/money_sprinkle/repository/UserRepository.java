package kakaopay.money_sprinkle.repository;

import kakaopay.money_sprinkle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
