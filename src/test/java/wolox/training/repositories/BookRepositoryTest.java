package wolox.training.repositories;


import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.Book;

@RunWith(SpringRunner.class)
@SpringBootTest
@DBRider
@DBUnit(caseSensitiveTableNames = true, escapePattern = "\"?\"" )
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DataSet("books.yml")
    public void whenInitializedByDbUnit_thenFindByPublisherAndGenreAndYear() {
        List<Book> bookList = bookRepository
            .findByPublisherAndGenreAndYear("Penguin", "Fantasia","2000");
        assertThat(bookList.size()).isEqualTo(2);
        assertThat(bookList.get(0).getTitle()).isEqualTo("Harry Potter y el prisionero de Azkaban");
        assertThat(bookList.get(1).getTitle()).isEqualTo("Misery");
    }

    @Test
    @DataSet("books.yml")
    public void whenInitializedByDbUnit_andYearNull_thenFindByPublisherAndGenreAndYear() {
        List<Book> bookList = bookRepository
            .findByPublisherAndGenreAndYear("Penguin", "Fantasia",null);
        assertThat(bookList.size()).isEqualTo(4);
    }

    @Test
    @DataSet("books.yml")
    public void whenInitializedByDbUnit_thenFindAll_withSomeParams() {
        List<Book> bookList = bookRepository
            .findAll(null,
                null, "2002", null, null, null, null, null);
        assertThat(bookList.size()).isEqualTo(1);
        assertThat(bookList.get(0).getTitle()).isEqualTo("La Torre Oscura");
    }

}
