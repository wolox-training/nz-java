package wolox.training.services.third_party;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import wolox.training.DTOs.BookDTO;
import wolox.training.exceptions.BookNotFoundException;

@Service
public class OpenLibraryService {

    private static final String DEFAULT_IMAGE = "IMAGE NOT AVAILABLE";
    @Value("${openlibrary.url}")
    private String base_url;

    public BookDTO bookInfo(String isbn) throws JsonProcessingException, BookNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder
            .fromHttpUrl(base_url)
            .queryParam("bibkeys", "ISBN:"+isbn)
            .queryParam("format","json")
            .queryParam("jscmd","data")
            .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        checkValidResponse(objectMapper, response);

        BookDTO bookDTO =  objectMapper.convertValue(
                objectMapper
                    .readTree(response.getBody())
                    .path("ISBN:" + isbn),
                BookDTO.class);
        bookDTO.setIsbn(isbn);
        bookDTO.setImage(
            getImage(objectMapper, response, isbn)
        );
        return bookDTO;
    }

    private void checkValidResponse(ObjectMapper objectMapper, ResponseEntity<String> response)
        throws JsonProcessingException, BookNotFoundException {
        if (response.getStatusCode() == HttpStatus.NOT_FOUND ||
            objectMapper.readTree(response.getBody()).size() == 0)
            throw new BookNotFoundException();
    }

    private String getImage(ObjectMapper objectMapper, ResponseEntity<String> response, String isbn)
        throws JsonProcessingException {
        try {
            return objectMapper
                .readTree(response.getBody())
                .path("ISBN:" + isbn)
                .path("cover")
                .get("small")
                .asText();
        } catch (NullPointerException e) {
            return DEFAULT_IMAGE;
        }
    }
}
