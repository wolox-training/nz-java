package wolox.training.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.DTOs.BookDTO;
import wolox.training.constants.ExceptionsConstants;
import wolox.training.constants.swagger.controllers.BookControllerConstants;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.third_party.OpenLibraryService;

@RestController
@RequestMapping("/api/books")
@Api
public class BookController {

  @Value("${openlibrary.url}")
  private String base_url;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private OpenLibraryService openLibraryService;

  @ApiOperation(value = BookControllerConstants.SHOW_DESCRIPTION, response = Book.class)
  @ApiResponses(value={
      @ApiResponse(code=200, message = BookControllerConstants.SUCCESSFUL_SHOW_RESPONSE),
      @ApiResponse(code=404, message = ExceptionsConstants.BOOK_NOT_FOUND)
  })
  @GetMapping("/{id}")
  public Book findOne(@ApiParam(value = BookControllerConstants.ID_DESCRIPTION, required = true) @PathVariable Long id) {
    return bookRepository.findById(id)
        .orElseThrow(BookNotFoundException::new);
  }

  @GetMapping()
  public List<Book> allBooks(@RequestParam Map<String,String> allParam) {
    return bookRepository.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookRepository.save(book);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    bookRepository.findById(id)
        .orElseThrow(BookNotFoundException::new);
    bookRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
    if (book.getId() != id) {
      throw new BookIdMismatchException();
    }
    bookRepository.findById(id)
        .orElseThrow(BookNotFoundException::new);
    return bookRepository.save(book);
  }

  @GetMapping("/isbn/{id}")
  public List<Book> findByIsbn(@PathVariable String id)
      throws JsonProcessingException, BookNotFoundException {
    BookDTO bookDTO = openLibraryService.bookInfo(id.toString());
    return bookRepository.findAll();
  }
}