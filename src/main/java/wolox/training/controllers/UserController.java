package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/{id}")
  public User findOne(@PathVariable Long id) {
    return userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);
    userRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  public User updateUser(@RequestBody User user, @PathVariable Long id)
      throws UserIdMismatchException {
    if (user.getId() != id) {
      throw new UserIdMismatchException();
    }
    User persistedUser = userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);

    persistedUser.setBirthDate(user.getBirthDate());
    persistedUser.setName(user.getName());
    persistedUser.setUsername(user.getUsername());

    return userRepository.save(persistedUser);
  }

  @PutMapping("/{id}/books/{bookId}")
  public User addBook(@PathVariable Long id, @PathVariable Long bookId)
      throws BookAlreadyOwnedException, BookNotFoundException, BookAlreadyOwnedException {

    Book book = bookRepository.findById(bookId)
        .orElseThrow(BookNotFoundException::new);

    User user = userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);

    user.addBook(book);

    return userRepository.save(user);
  }

  @DeleteMapping("/{id}/books/{bookId}")
  public User removeBook(@PathVariable Long id, @PathVariable Long bookId)
      throws BookNotFoundException, BookAlreadyOwnedException {

    Book book = bookRepository.findById(bookId)
        .orElseThrow(BookNotFoundException::new);

    User user = userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);

    user.removeBook(book);

    return userRepository.save(user);
  }

  @PutMapping("/{id}/password")
  public User updatePassword(@RequestBody User user, @PathVariable Long id)
      throws UserIdMismatchException {
    if (user.getId() != id) {
      throw new UserIdMismatchException();
    }

    User persistedUser = userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);

    persistedUser.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(persistedUser);

  }
}
