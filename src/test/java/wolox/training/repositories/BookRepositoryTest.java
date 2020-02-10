package wolox.training.repositories;


import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import java.awt.print.Pageable;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        Page<Book> bookList = bookRepository
            .findByPublisherAndGenreAndYear("Penguin",
                "Fantasia","2000", PageRequest.of(0,5));
        assertThat(bookList.getNumberOfElements()).isEqualTo(2);
        assertThat(bookList.getContent().get(0).getTitle()).isEqualTo("Harry Potter y el prisionero de Azkaban");
        assertThat(bookList.getContent().get(1).getTitle()).isEqualTo("Misery");
    }

    @Test
    @DataSet("books.yml")
    public void whenInitializedByDbUnit_andYearNull_thenFindByPublisherAndGenreAndYear() {
        Page<Book> bookList = bookRepository
            .findByPublisherAndGenreAndYear("Penguin",
                "Fantasia",null, PageRequest.of(0,5));
        assertThat(bookList.getNumberOfElements()).isEqualTo(4);
    }

    @Test
    @DataSet("books.yml")
    public void whenInitializedByDbUnit_thenFindAll_withSomeParams() {
        Page<Book> bookPage = bookRepository.findAll(null, null,
            "2002", null, null, null, null,
            null, PageRequest.of(0,5, Sort.by("year").descending()));
        assertThat(bookPage.getNumberOfElements()).isEqualTo(1);
        assertThat(bookPage.getContent().get(0).getTitle()).isEqualTo("La Torre Oscura");
    }

}
