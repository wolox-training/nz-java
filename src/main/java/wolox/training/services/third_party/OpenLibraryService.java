package wolox.training.services.third_party;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import wolox.training.DTOs.BookDTO;
import wolox.training.exceptions.BookNotFoundException;

@Resource
public class OpenLibraryService {

    @Value("${openlibrary.url}")
    public String base_url;

    public BookDTO bookInfo(String isbn) throws JsonProcessingException, BookNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder
            .fromHttpUrl(base_url)
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
