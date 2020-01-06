package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.constants.ExceptionsConstants;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ExceptionsConstants.BOOK_NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super(ExceptionsConstants.BOOK_NOT_FOUND, new Exception());
    }
}