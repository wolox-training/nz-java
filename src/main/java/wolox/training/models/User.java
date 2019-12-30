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

  public List<Book> getBooks() {
    return (List<Book>) Collections.unmodifiableList(books);
  }
}
