package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wolox.training.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  public Optional<User> findByName(String name);
  public Optional<User> findByUsername(String username);

  public List<User> findByBirthDateBetweenAndNameContainingIgnoreCase(
      LocalDate firstDate,
      LocalDate secondDate,
      String name);
}