package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.BookRepository;
import wolox.training.support.factories.BookFactory;
import static org.hamcrest.MatcherAssert.assertThat;

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

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutAuthor_thenThrowException() {
    book.setAuthor(null);
    bookRepository.save(book);
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutImage_thenThrowException() {
    book.setImage(null);
    bookRepository.save(book);
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutTitle_thenThrowException() {
    book.setTitle(null);
    bookRepository.save(book);
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutPublisher_thenThrowException() {
    book.setPublisher(null);
    bookRepository.save(book);
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutYear_thenThrowException() {
    book.setYear(null);
    bookRepository.save(book);
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutPages_thenThrowException() {
    book.setPages(null);
    bookRepository.save(book);
  }

  @Test(expected = NullPointerException.class)
  public void whenCreateBookWithoutIsbn_thenThrowException() {
    book.setIsbn(null);
    bookRepository.save(book);
  }

  @Test
  public void whenFindByAuthor_thenReturnBook() {
    // when
    Book found = bookRepository.findByAuthor(book.getAuthor()).get();

    // then
    assertThat(found.getAuthor())
        .isEqualTo(book.getAuthor());
  }

  @Test
  public void whenFindByAuthor_thenReturnNull() {
    // when
    Optional<Book> found = bookRepository.findByAuthor("pepe");

    // then
    assertThat(
        found.isEmpty()
    ).isTrue();
  }

  @Test
  public void whenFindAll_thenReturnListOfBooks() {
    Book book_2 = bookFactory.title("My last book").build();
    Book book_3 = bookFactory.title("My last book V2").build();

    entityManager.persist(book_2);
    entityManager.persist(book_3);
    entityManager.flush();

    // when
    List<Book> found = bookRepository.findAll();

    // then
    assertThat(found, hasItems(book, book_2, book_3));
  }

}
