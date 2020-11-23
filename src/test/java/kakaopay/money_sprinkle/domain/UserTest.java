package kakaopay.money_sprinkle.domain;

import kakaopay.money_sprinkle.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test_사용자_생성() throws Exception {
        // given
        User user = new User("user1");
        userRepository.save(user);

        // when
        User userFound = userRepository.findById(user.getId()).orElseThrow();

        // then
        assertThat(userFound).isEqualTo(user);
    }
}