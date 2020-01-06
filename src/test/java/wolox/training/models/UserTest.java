package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.repositories.UserRepository;
import wolox.training.support.factories.BookFactory;
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
  private Book book1;

  @Before
  public void setUp() {
    user = new UserFactory().build();
    book1 = new BookFactory().build();

    user.addBook(book1);
    
    entityManager.persist(user);
    entityManager.flush();
  }

  @Test
  public void whenCreateUser_thenUserIsPersisted() {
    // when
    User persistedUser = userRepository.findByName(user.getName()).get();

    // then
    assertThat(persistedUser.getUsername())
        .isEqualTo(user.getUsername());
    assertThat(persistedUser.getName())
        .isEqualTo(user.getName());
    assertThat(persistedUser.getBirthDate())
        .isEqualTo(user.getBirthDate());
    assertThat(persistedUser.getBooks().size())
        .isEqualTo(user.getBooks().size());
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateUserWithoutUsername_thenThrowException() {
    user.setUsername(null);
    userRepository.save(user);
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateUserWithoutName_thenThrowException() {
    // when
    user.setName(null);
    userRepository.save(user);
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateUserWithoutBirthDate_thenThrowException() {
    // when
    user.setBirthDate(null);
    userRepository.save(user);
  }

  @Test(expected = BookAlreadyOwnedException.class)
  public void whenAddingAnAlreadyAddedBook_thenThrowException() {
    user.addBook(book1);
    userRepository.save(user);
  }

  @Test
  public void whenFindByName_thenReturnUser() {
    // when
    User found = userRepository.findByName(user.getName()).get();

    // then
    assertThat(found.getName())
        .isEqualTo(user.getName());
  }

  @Test
  public void whenFindByNameWithAnInvalidId_thenReturnNull() {
    // when
    Optional<User> found = userRepository.findByName("pepe");

    // then
    assertThat(
        found.isEmpty()
    ).isTrue();
  }


}