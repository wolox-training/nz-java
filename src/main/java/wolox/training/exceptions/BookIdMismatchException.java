package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.constants.ExceptionsConstants;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = ExceptionsConstants.BOOK_ID_MISMATCH)
public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {
        super(ExceptionsConstants.BOOK_ID_MISMATCH, new Exception());
    }
}