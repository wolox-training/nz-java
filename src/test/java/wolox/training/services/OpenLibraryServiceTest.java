package wolox.training.services;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.DTOs.BookDTO;
import wolox.training.services.third_party.OpenLibraryService;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class,
    classes = { OpenLibraryService.class })
@ActiveProfiles("test")
public class OpenLibraryServiceTest {

    @Autowired
    OpenLibraryService openLibraryService;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void whenCallingBookInfo_andTheBookExist_thenReturnTheBookDTOAsociated()
        throws IOException {
        stubFor(
            get(urlEqualTo("/api/books?bibkeys=ISBN:123456789&format=json&jscmd=data"))
            .willReturn(aResponse()
              .withStatus(200).withHeader("Content-Type", "application/json")
                .withBody(
                  new String(
                      Files.readAllBytes(
                          Paths.get("./src/test/java/wolox/training"
                              + "/support/mocks/open_library/book_without_image.json")
                      )
                  )
              )
            )
        );

        BookDTO bookDTO = openLibraryService.bookInfo("123456789");

        assertThat(bookDTO.getTitle())
            .isEqualTo("How equal temperament ruined harmony (and why you should care)");
        assertThat(bookDTO.getGenre()).isBlank();
        assertThat(bookDTO.getIsbn()).isEqualTo("123456789");
        assertThat(bookDTO.getPages()).isEqualTo(196);
        assertThat(bookDTO.getPublishDate()).isEqualTo("2007");
        assertThat(bookDTO.getSubtitle()).isBlank();

        assertThat(bookDTO.getPublishers().length).isEqualTo(1);
        assertThat(bookDTO.getPublishers()[0].getName()).isEqualTo("W. W. Norton");

        assertThat(bookDTO.getAuthors().length).isEqualTo(1);
        assertThat(bookDTO.getAuthors()[0].getName()).isEqualTo("Ross W. Duffin");


    }

}
