package wolox.training.repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
  @Query("SELECT book from Book book WHERE book.author = :author LIMIT 1")
  public Book findByAuthor(
      @Param("author") String author);
}
