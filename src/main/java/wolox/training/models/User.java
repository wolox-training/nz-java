package wolox.training.models;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;

@Entity
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  private long id;

  @Column(nullable = false)
  @Getter @Setter
  private String username;

  @Column(nullable = false)
  @Getter @Setter
  private String name;

  @Column(nullable = false)
  @Getter @Setter
  private LocalDate birthDate;

  @OneToMany(mappedBy = "user")
  @NotBlank
  @Setter
  private List<Book> books;

  public void addBook(Book book) throws BookAlreadyOwnedException {
    if(this.books.contains(book)) {
      throw new BookAlreadyOwnedException();
    } else {
      this.books.add(book);
    }
  }

  public void removeBook(Book book) {
    if(!this.books.contains(book)) {
      throw new BookNotFoundException();
    } else {
      this.books.remove(book);
    }
  }

  public List<Book> getBooks() {
    return (List<Book>) Collections.unmodifiableList(books);
  }
}
