package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Book not found")
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super("Book not found", new Exception());
    }
}