package wolox.training.support.factories;

import com.github.javafaker.Faker;
import java.util.List;
import wolox.training.DTOs.AuthorDTO;
import wolox.training.DTOs.BookDTO;
import wolox.training.DTOs.PublisherDTO;

public class BookDTOFactory {

    Faker faker = new Faker();

    private String isbn = faker.number().digits(10);
    private String title = faker.book().title();
    private String subtitle = faker.book().title();
    private String genre = faker.book().genre();
    private List<PublisherDTO> publishers;
    private String publishDate = Integer.toString(faker.number().numberBetween(1900,2020));
    private String pagination;
    private List<AuthorDTO> authors;

    public BookDTO build() {
        return new BookDTO(
            this.isbn,
            this.title,
            this.subtitle,
            this.genre,
            this.publishers,
            this.publishDate,
            this.pagination,
            this.authors);
    };

    public BookDTOFactory pagination(String pagination) {
        this.pagination = pagination;
        return this;
    }

    public BookDTOFactory authors(List<AuthorDTO> author_list) {
        this.authors = author_list;
        return this;
    }

    public BookDTOFactory publisers(List<PublisherDTO> publisher_list) {
        this.publishers = publisher_list;
        return this;
    }
}
