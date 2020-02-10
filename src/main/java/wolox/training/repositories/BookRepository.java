package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public Page<Book> findByPublisherAndGenreAndYear(
      @Param("publisher") String publisher,
      @Param("genre") String genre,
      @Param("year") String year,
      Pageable pageable);

  @Query("SELECT b FROM Book b WHERE "
      + "(:publisher IS NULL OR b.publisher = :publisher) AND "
      + "(:genre IS NULL OR b.genre = :genre) AND "
      + "(:year IS NULL OR b.year = :year) AND "
      + "(:author IS NULL OR b.author = :author) AND "
      + "(:image IS NULL OR b.image = :image) AND "
      + "(:title IS NULL OR b.title = :title) AND "
      + "(:pages IS NULL OR b.pages = :pages) AND "
      + "(:isbn IS NULL OR b.isbn = :isbn)")
  public Page<Book> findAll(
      @Param("publisher") String publisher,
      @Param("genre") String genre,
      @Param("year") String year,
      @Param("author") String author,
      @Param("image") String image,
      @Param("title") String title,
      @Param("pages") Integer pages,
      @Param("isbn") String isbn,
      Pageable pageable);
}
