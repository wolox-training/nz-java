package wolox.training.services;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Rule;
import org.junit.Test;
import wolox.training.DTOs.BookDTO;
import wolox.training.constants.services.OpenLibraryConstants;
import wolox.training.services.third_party.OpenLibraryService;

public class OpenLibraryServiceTest {

    private OpenLibraryService openLibraryService = new OpenLibraryService();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(433);

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

    }

}
