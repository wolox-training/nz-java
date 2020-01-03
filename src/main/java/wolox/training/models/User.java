package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity
@Table(name = "users", schema = "public")
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

  @JsonManagedReference
  @OneToMany(mappedBy = "user")
  @Setter
  private List<Book> books;

  public void addBook(Book book) throws BookAlreadyOwnedException {
    if(books.contains(book)) {
      throw new BookAlreadyOwnedException();
    } else {
      book.setUser(this);
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
}
