package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.constants.ExceptionsConstants;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = ExceptionsConstants.BOOK_ALREADY_OWN)
public class BookAlreadyOwnedException extends RuntimeException {

  public BookAlreadyOwnedException() {
    super(ExceptionsConstants.BOOK_ALREADY_OWN, new Exception());
  }
}
