package wolox.training.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import java.time.LocalDate;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@DBRider
@DBUnit(caseSensitiveTableNames = true, escapePattern = "\"?\"" )
@ActiveProfiles("test")
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @DataSet("users.yml")
  public void whenInitializedByDbUnit_thenFindByBirthDateBetweenAndNameContainingIgnoreCase() {
    Page<User> userList = userRepository
        .findByBirthDateBetweenAndNameContainingIgnoreCase(
            LocalDate.of(1995,01,01),
            LocalDate.of(1995,01,30),"epe",
            PageRequest.of(0,5));
    assertThat(userList.getNumberOfElements()).isEqualTo(1);
    assertThat(userList.getContent().get(0).getName()).isEqualTo("Pepe");
  }

  @Test
  @DataSet("users.yml")
  public void whenInitializedByDbUnit_andNameIsNull_thenFindByBirthDateBetweenAndNameContainingIgnoreCase() {
    Page<User> userList = userRepository
        .findByBirthDateBetweenAndNameContainingIgnoreCase(
            LocalDate.of(1995,01,01),
            LocalDate.of(1995,01,30),null,
            PageRequest.of(0,5));
    assertThat(userList.getNumberOfElements()).isEqualTo(3);
  }

}
