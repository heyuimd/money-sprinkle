package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test_사용자_생성() throws Exception {
        // given
        String loginId = "user1";
        String userName = "홍길동";
        User user = User.createUser(loginId, userName);
        userRepository.save(user);

        // when
        User userFound = userRepository.findById(user.getId()).orElseThrow();

        // then
        assertThat(userFound).isEqualTo(user);
        assertThat(userFound.getLoginId()).isEqualTo(loginId);
        assertThat(userFound.getName()).isEqualTo(userName);
        assertThat(userFound.getMoney()).isGreaterThan(0L);
    }
}