package kakaopay.money_sprinkle.repository;

import kakaopay.money_sprinkle.domain.MoneySprinkle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MoneySprinkleRepository extends JpaRepository<MoneySprinkle, Long> {

    @Query("select ms from MoneySprinkle ms left join fetch ms.sprinkledMoneyList where ms.token = :token")
    Optional<MoneySprinkle> findByToken(@Param("token") String token);

    @Query("select ms from MoneySprinkle ms left join fetch ms.sprinkledMoneyList " +
            "where ms.token = :token and ms.user.id = :userId")
    Optional<MoneySprinkle> findByTokenAndUserId(@Param("token") String token, @Param("userId") Long userId);
}
