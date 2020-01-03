package wolox.training.models;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wolox.training.constants.PreconditionsConstants;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity
@ApiModel(description = "User model in the WBooks API")
@Table(name = "users", schema = "public")
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  private long id;

  @Column(nullable = false)
  @ApiModelProperty(notes = "The user username, identifies a user by it's username")
  @Getter
  private String username;

  @Column(nullable = false)
  @ApiModelProperty(notes = "The user name")
  @Getter
  private String name;

  @Column(nullable = false)
  @ApiModelProperty(notes = "The user birth date")
  @Getter
  private LocalDate birthDate;

  @OneToMany(cascade = CascadeType.ALL)
  @ApiModelProperty(notes = "The user's rent books")
  @JoinColumn(name = "users_id")
  @JsonManagedReference
  @Setter
  private List<Book> books;

  public void addBook(Book book) throws BookAlreadyOwnedException {
    if(books.contains(book)) {
      throw new BookAlreadyOwnedException();
    } else {
      books.add(book);
    }
  }

  public void removeBook(Book book) {
    this.books.removeIf(
        associatedBook->associatedBook.getId() == book.getId()
    );
  }

  public List<Book> getBooks() {
    return (List<Book>) Collections.unmodifiableList(books);
  }


  public void setBirthDate(LocalDate birthDate) {
    Preconditions.checkNotNull(birthDate, PreconditionsConstants.NOT_NULL_MESSAGE, "birth date");
    this.birthDate = birthDate;
  }

  public void setName(String name) {
    Preconditions.checkNotNull(name, PreconditionsConstants.NOT_NULL_MESSAGE, "name");
    this.name = name;
  }

  public void setUsername(String username) {
    Preconditions.checkNotNull(username, PreconditionsConstants.NOT_NULL_MESSAGE, "username");
    this.username = username;
  }
}
