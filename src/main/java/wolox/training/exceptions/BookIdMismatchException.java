package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Book and id mismatch")
public class BookIdMismatchException extends RuntimeException {
    public BookIdMismatchException() {
        super("Book and id mismatch", new Exception());
    }
}