package wolox.training.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository {
  @Query("SELECT book from Book book WHERE book.author = :author")
  public Book findByAuthor(
      @Param("author") String author);
}
