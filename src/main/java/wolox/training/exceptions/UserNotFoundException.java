package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.constants.ExceptionsConstants;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ExceptionsConstants.USER_NOT_FOUND)
public class UserNotFoundException extends RuntimeException  {
  public UserNotFoundException() {
    super(ExceptionsConstants.USER_NOT_FOUND, new Exception());
  }
}
