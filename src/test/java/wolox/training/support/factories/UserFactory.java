package wolox.training.support.factories;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import wolox.training.models.Book;
import wolox.training.models.User;

public class UserFactory {

  Faker faker = new Faker();

  private String username = faker.name().username();
  private String name = faker.name().fullName();
  private LocalDate birthDate = faker.date()
      .birthday()
      .toInstant()
      .atZone(
          ZoneId
              .systemDefault()
      )
      .toLocalDate();
  private List<Book> books = new ArrayList<Book>();
  private String password = "1234567890";

  public User build() {
    return new User(
        this.username,
        this.name,
        this.birthDate,
        this.books,
        this.password);
  }

  public UserFactory name(String name) {
    this.name = name;
    return this;
  }

  public UserFactory username(String username) {
    this.username = username;
    return this;
  }

  public UserFactory birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public UserFactory password(String password) {
    this.password = password;
    return this;
  }
}
