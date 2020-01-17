package wolox.training.repositories;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.Book;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Transactional
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        entityManager.flush();
    }

    @Test
    void whenInitializedByDbUnit_thenFindByPublisherAndGenreAndYear() {
        List<Book> books = bookRepository.findAll();
        //List<Book> books = bookRepository.findByPublisherAndGenreAndYear(
        //    "Penguin",
        //    "Fantasia",
        //    "2000");
        assertThat(books.size()).isEqualTo(1);
    }

}
