package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.constants.ExceptionsConstants;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = ExceptionsConstants.USER_ID_MISMATCH)
public class UserIdMismatchException extends Throwable {
  public UserIdMismatchException() {
    super(ExceptionsConstants.USER_ID_MISMATCH, new Exception());
  }
}
