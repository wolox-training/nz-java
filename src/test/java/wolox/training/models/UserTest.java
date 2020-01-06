package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.UserRepository;
import wolox.training.support.factories.UserFactory;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  private User user;

  @Before
  public void setUp() {
    user = new UserFactory().build();
    entityManager.persist(user);
    entityManager.flush();
  }
  
  @Test
  public void whenFindByName_thenReturnUser() {
    // when
    User found = userRepository.findByName(user.getName()).get();

    // then
    assertThat(found.getName())
        .isEqualTo(user.getName());
  }
}