package wolox.training.DTOs;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import wolox.training.models.Book;

@Data
@AllArgsConstructor
public class BookDTO{
    private String isbn;
    private String title;
    private String subtitle;
    private String genre;
    private List<PublisherDTO> publishers;
    private String publishDate;
    private String pagination;
    private List<AuthorDTO> authors;

    public Book toModel() {
        return new Book(
            this.genre,
            stringifyList(this.authors),
            this.title,
            stringifyList(this.publishers),
            this.publishDate,
            pagesToInt(),
            this.isbn
        );
    }

    private Integer pagesToInt() {
        String[] splited = pagination.split("\\s+");
        return Integer.parseInt(splited[0]);
    }

    private String stringifyList(List<? extends DTO> list) {
        return list.stream()
                   .map(element -> element.getName())
                   .collect(Collectors.joining(", "));
    }
}
