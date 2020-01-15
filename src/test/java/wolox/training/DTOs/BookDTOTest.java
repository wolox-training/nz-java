package wolox.training.DTOs;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import wolox.training.models.Book;

public class BookDTOTest {

    private BookDTO bookDTO;
    private AuthorDTO[] author_list;
    private PublisherDTO[] publisher_list;

    @Before
    public void setUp() {
        Faker faker = new Faker();

        AuthorDTO nico = new AuthorDTO();
        nico.setName("Nicolas Zarewsky");

        AuthorDTO rodri = new AuthorDTO();
        rodri.setName("Rodrigo Francou");

        AuthorDTO jime = new AuthorDTO();
        jime.setName("Jimena Rosello");

        author_list = new AuthorDTO[]{ nico, rodri, jime };

        PublisherDTO penguin = new PublisherDTO();
        penguin.setName("Penguin");

        PublisherDTO wales = new PublisherDTO();
        wales.setName("Wales");

        publisher_list = new PublisherDTO[]{ penguin, wales };

        bookDTO = new BookDTO();
        bookDTO.setAuthors(author_list);
        bookDTO.setPublishers(publisher_list);
        bookDTO.setGenre(faker.book().genre());
        bookDTO.setPublishDate(Integer.toString(faker.number().numberBetween(1900,2020)));
        bookDTO.setPages(faker.number().numberBetween(100,5000));
        bookDTO.setTitle(faker.book().title());
        bookDTO.setSubtitle(faker.book().title());
        bookDTO.setIsbn(faker.number().digits(10));
        bookDTO.setImage("pepe");
    }

    @Test
    public void whenToModel_thenItCreatesABookInstance() {
        Book book = bookDTO.toModel();

        // then
        assertThat(book.getGenre())
            .isEqualTo(bookDTO.getGenre());

        assertThat(book.getAuthor())
            .isEqualTo("Nicolas Zarewsky, Rodrigo Francou, Jimena Rosello");

        assertThat(book.getImage()).isEqualTo(bookDTO.getImage());

        assertThat(book.getTitle())
            .isEqualTo(bookDTO.getTitle());

        assertThat(book.getPublisher())
            .isEqualTo("Penguin, Wales");

        assertThat(book.getYear())
            .isEqualTo(bookDTO.getPublishDate());

        assertThat(book.getPages())
            .isEqualTo(bookDTO.getPages());

        assertThat(book.getIsbn())
            .isEqualTo(bookDTO.getIsbn());

    }
}
