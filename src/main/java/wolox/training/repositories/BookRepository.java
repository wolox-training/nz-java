package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
  public Optional<Book> findByAuthor(String author);
  public Optional<Book> findByIsbn(String isbn_number);

  @Query("SELECT b FROM Book b WHERE "
      + "(:publisher IS NULL OR b.publisher = :publisher) and "
      + "(:genre IS NULL OR b.genre = :genre) and"
      + "(:year IS NULL OR b.year = :year)")
  public List<Book> findByPublisherAndGenreAndYear(
      @Param("publisher") String publisher,
      @Param("genre") String genre,
      @Param("year") String year);


  public List<Book> findAll();
}
