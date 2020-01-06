package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
  public Optional<User> findByName(String name);
}