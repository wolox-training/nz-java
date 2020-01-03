package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
  public Optional<Book> findByAuthor(String author);
  public List<Book> findAll();
}
