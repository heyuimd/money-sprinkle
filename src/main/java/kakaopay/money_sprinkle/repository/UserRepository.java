package kakaopay.money_sprinkle.repository;

import kakaopay.money_sprinkle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.roomInOutList")
    Optional<User> findByIdWithRoomInOut(Long id);
}
