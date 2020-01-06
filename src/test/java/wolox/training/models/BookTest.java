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
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.support.factories.BookFactory;
import wolox.training.support.factories.UserFactory;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class BookTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private BookRepository bookRepository;

  private Book book;
  private BookFactory bookFactory = new BookFactory();

  @Before
  public void setUp() {
    book = bookFactory.build();

    entityManager.persist(book);
    entityManager.flush();
  }

  @Test
  public void whenCreateBook_thenBookIsPersisted() {
    // when
    Book persistedBook = bookRepository.findById(book.getId()).get();

    // then
    assertThat(persistedBook.getGenre())
        .isEqualTo(book.getGenre());
    assertThat(persistedBook.getAuthor())
        .isEqualTo(book.getAuthor());
    assertThat(persistedBook.getImage())
        .isEqualTo(book.getImage());
    assertThat(persistedBook.getTitle())
        .isEqualTo(book.getTitle());
    assertThat(persistedBook.getPublisher())
        .isEqualTo(book.getPublisher());
    assertThat(persistedBook.getYear())
        .isEqualTo(book.getYear());
    assertThat(persistedBook.getPages())
        .isEqualTo(book.getPages());
    assertThat(persistedBook.getIsbn())
        .isEqualTo(book.getIsbn());
  }

}
