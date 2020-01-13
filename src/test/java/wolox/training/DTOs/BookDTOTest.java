package wolox.training.DTOs;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.models.auth.In;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import wolox.training.models.Book;
import wolox.training.support.factories.BookDTOFactory;
import wolox.training.support.factories.BookFactory;

public class BookDTOTest {

    private BookDTO bookDTO;
    private List<AuthorDTO> author_list;
    private List<PublisherDTO> publisher_list;
    private BookDTOFactory bookDTOfactory = new BookDTOFactory();

    @Before
    public void setUp() {
        author_list = Arrays.asList(new AuthorDTO[]{
            new AuthorDTO("Nicolas Zarewsky"),
            new AuthorDTO("Rodrigo Francou"),
            new AuthorDTO("Jimena Rosello")
        });

        publisher_list = Arrays.asList(new PublisherDTO[]{
            new PublisherDTO("Penguin"),
            new PublisherDTO("Wales")
        });

        bookDTO = bookDTOfactory
            .pagination("196 p. :")
            .authors(author_list)
            .publisers(publisher_list)
            .build();
    }

    @Test
    public void whenCallingPagesToInt_thenTransformedTheStringPagesToInt()
        throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // when
        Method method = BookDTO.class.getDeclaredMethod("pagesToInt");
        method.setAccessible(true);
        Integer result = (Integer) method.invoke(bookDTO);

        // then
        assertThat(result).isEqualTo(196);
    }

    @Test
    public void whenCallingStringifyListOnAuthors_thenTransformedTheListOfDTOsIntoAString()
        throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // when
        Method method = BookDTO.class.getDeclaredMethod("stringifyList", List.class);
        method.setAccessible(true);
        String result = (String) method.invoke(bookDTO, author_list);

        // then
        assertThat(result).isEqualTo("Nicolas Zarewsky, Rodrigo Francou, Jimena Rosello");
    }

    @Test
    public void whenCallingStringifyListOnPublishers_thenTransformedTheListOfDTOsIntoAString()
        throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // when
        Method method = BookDTO.class.getDeclaredMethod("stringifyList", List.class);
        method.setAccessible(true);
        String result = (String) method.invoke(bookDTO, publisher_list);

        // then
        assertThat(result).isEqualTo("Penguin, Wales");
    }

    @Test
    public void whenToModel_thenItCreatesABookInstance() {
        Book book = bookDTO.toModel();

        // then
        assertThat(book.getGenre())
            .isEqualTo(bookDTO.getGenre());

        assertThat(book.getAuthor())
            .isEqualTo("Nicolas Zarewsky, Rodrigo Francou, Jimena Rosello");

        assertThat(book.getImage()).isBlank();

        assertThat(book.getTitle())
            .isEqualTo(bookDTO.getTitle());

        assertThat(book.getPublisher())
            .isEqualTo("Penguin, Wales");

        assertThat(book.getYear())
            .isEqualTo(bookDTO.getPublishDate());

        assertThat(book.getPages())
            .isEqualTo(196);

        assertThat(book.getIsbn())
            .isEqualTo(bookDTO.getIsbn());

    }
}
