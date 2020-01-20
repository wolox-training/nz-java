package wolox.training.repositories;


import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DBRider
@DBUnit(caseSensitiveTableNames = true, escapePattern = "\"?\"" )
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void setUp() {};

    @Test
    @DataSet("books.yml")
    public void whenInitializedByDbUnit_thenFindByPublisherAndGenreAndYear() {
        assertThat(bookRepository.count()).isEqualTo(4);
    }

}
