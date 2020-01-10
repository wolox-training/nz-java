package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import wolox.training.constants.PreconditionsConstants;
import wolox.training.constants.swagger.models.UserConstants;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity
@ApiModel(description = UserConstants.MODEL_DESCRIPTION)
@Table(name = "users", schema = "public")
@NoArgsConstructor
public class User {

  @Transient
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  private long id;

  @Column(nullable = false)
  @ApiModelProperty(notes = UserConstants.USERNAME_DESCRIPTION)
  @Getter
  private String username;

  @Column(nullable = false)
  @ApiModelProperty(notes = UserConstants.NAME_DESCRIPTION)
  @Getter
  private String name;

  @Column(nullable = false)
  @ApiModelProperty(notes = UserConstants.BIRTH_DATE_DESCRIPTION)
  @Getter
  private LocalDate birthDate;

  @OneToMany(cascade = CascadeType.ALL)
  @ApiModelProperty(notes = UserConstants.BOOKS_DESCRIPTION)
  @JoinColumn(name = "users_id")
  @JsonManagedReference
  @Setter
  private List<Book> books;

  @Column(nullable = false)
  @Getter
  private String password;

  public User(String username, String name, LocalDate birthDate, List<Book> books, String password) {
    this.setUsername(username);
    this.setName(name);
    this.setBirthDate(birthDate);
    this.setBooks(books);
    this.setPassword(password);
  }

  public void addBook(Book book) throws BookAlreadyOwnedException {
    if(books.contains(book)) {
      throw new BookAlreadyOwnedException();
    } else {
      books.add(book);
    }
  }

  public void removeBook(Book book) {
    this.books.removeIf(
        associatedBook -> associatedBook.getId() == book.getId()
    );
  }

  public List<Book> getBooks() {
    return (List<Book>) Collections.unmodifiableList(books);
  }


  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = Preconditions.checkNotNull(birthDate, PreconditionsConstants.NOT_NULL_MESSAGE, "birth date");
  }

  public void setName(String name) {
    this.name = Preconditions.checkNotNull(name, PreconditionsConstants.NOT_NULL_MESSAGE, "name");
  }

  public void setUsername(String username) {
    this.username = Preconditions.checkNotNull(username, PreconditionsConstants.NOT_NULL_MESSAGE, "username");
  }

  public void setPassword(String password) {
    this.password = passwordEncoder.encode(
        Preconditions.checkNotNull(
            password,
            PreconditionsConstants.NOT_NULL_MESSAGE,
            "password")
    );
  }
}
