package wolox.training.services.third_party;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import wolox.training.DTOs.BookDTO;
import wolox.training.constants.services.OpenLibraryConstants;
import wolox.training.exceptions.BookNotFoundException;

public class OpenLibraryService {

    public BookDTO bookInfo(String isbn) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder
            .fromHttpUrl(OpenLibraryConstants.URL)
            .queryParam("bibkeys", "ISBN:"+isbn)
            .queryParam("format","json")
            .queryParam("jscmd","data")
            .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            throw new BookNotFoundException();

        ObjectMapper objectMapper = new ObjectMapper();
        BookDTO bookDTO =  objectMapper.convertValue(
                objectMapper
                    .readTree(response.getBody())
                    .path("ISBN:" + isbn),
                BookDTO.class);
        bookDTO.setIsbn(isbn);
        return bookDTO;
    }
}
