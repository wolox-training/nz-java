package wolox.training.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import wolox.training.models.Book;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO implements Serializable {
    @JsonProperty
    private String isbn;
    @JsonProperty
    private String title;
    @JsonProperty
    private String subtitle;
    @JsonProperty
    private String genre;
    @JsonProperty
    private PublisherDTO[] publishers;
    @JsonProperty("publish_date")
    private String publishDate;
    @JsonProperty("number_of_pages")
    private int pages;
    @JsonProperty
    private AuthorDTO[] authors;
    private String image;

    public Book toModel() {
        return new Book(
            this.genre,
            stringifyList(Arrays.asList(this.authors)),
            this.title,
            stringifyList(Arrays.asList(this.publishers)),
            this.publishDate,
            this.pages,
            this.isbn,
            this.image
        );
    }

    private String stringifyList(List<? extends DTO> list) {
        return list.stream()
                   .map(element -> element.getName())
                   .collect(Collectors.joining(", "));
    }
}
