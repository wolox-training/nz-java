package wolox.training.services.authentication;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Component
public class UserAuthProvider implements AuthenticationProvider {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {

    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);;

    if (passwordEncoder.matches(password, user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(
          username, password, new ArrayList<>());
    } else {
      return null;
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
