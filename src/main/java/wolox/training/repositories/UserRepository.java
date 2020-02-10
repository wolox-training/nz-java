package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  public Optional<User> findByName(String name);
  public Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u WHERE "
      + "( CAST(:firstDate AS date) IS NULL OR CAST(:secondDate AS date) IS NULL OR"
      + " u.birthDate BETWEEN :firstDate AND :secondDate)"
      + " AND ( CAST(:name AS text) IS NULL OR LOWER(u.name) LIKE "
      + "CONCAT('%', LOWER(CAST(:name AS text)), '%'))"
  )
  public Page<User> findByBirthDateBetweenAndNameContainingIgnoreCase(
      @Param("firstDate") LocalDate firstDate,
      @Param("secondDate") LocalDate secondDate,
      @Param("name") String name,
      Pageable pageable);
}