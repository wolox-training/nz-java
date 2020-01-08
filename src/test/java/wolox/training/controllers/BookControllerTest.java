package wolox.training.controllers;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.support.factories.BookFactory;
import wolox.training.support.factories.UserFactory;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(value = BookController.class)
@ContextConfiguration(classes = {BookController.class})
public class BookControllerTest {
  @Autowired
  MockMvc mvc;

  @MockBean
  BookRepository mockBookRepository;

  private Book book;

  @Before
  public void setUp() {
    book = new BookFactory()
        .author("J.K. Rowling")
        .title("Harry Potter y la piedra filosofal")
        .genre("Fantasia")
        .image("shorturl.at/aCFR8")
        .isbn("9788700631625")
        .pages(310)
        .publisher("Penguin")
        .year("1998")
        .build();
  }

  @Test
  public void whenFindByIdWhichExists_thenBookIsReturned() throws Exception {
    when(mockBookRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(book));
    String url = ("/api/books/1");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\"id\":0,\"genre\":\"Fantasia\","
                + "\"author\":\"J.K. Rowling\",\"image\":\"shorturl.at/aCFR8\",\"title\":\""
                + "Harry Potter y la piedra filosofal\",\"publisher\":\"Penguin\",\"year\":"
                + "\"1998\",\"pages\":310,\"isbn\":\"9788700631625\"}"
        ));
  }

  @Test
  public void whenFindByIdWhichDoesNotExists_thenNoBookIsReturned() throws Exception {
    String url = ("/api/books/1");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenFindAll_thenReturnAllBooks() throws Exception {
    Book book_2 = new BookFactory()
        .title("Harry Potter y la camara de los secretos")
        .build();

    Book book_3 = new BookFactory()
        .title("Harry Potter y el prisionero de azkaban")
        .build();

    List<Book> allBooks = Arrays.asList(new Book[]{book, book_2, book_3});

    when(mockBookRepository.findAll()).thenReturn(allBooks);

    String url = ("/api/books");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].title").value("Harry Potter y"
            + " la piedra filosofal"))
        .andExpect(jsonPath("$[1].title").value("Harry Potter y la camara "
            + "de los secretos"))
        .andExpect(jsonPath("$[2].title").value("Harry Potter y "
            + "el prisionero de azkaban"));
  }

  @Test
  public void whenCreateBook_thenBookIsCreated() throws Exception {
    String url = ("/api/books");
    mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content("{\"id\":0,\"genre\":\"Fantasia\","
                + "\"author\":\"J.K. Rowling\",\"image\":\"shorturl.at/aCFR8\",\"title\":\""
                + "Harry Potter y la piedra filosofal\",\"publisher\":\"Penguin\",\"year\":"
                + "\"1998\",\"pages\":310,\"isbn\":\"9788700631625\"}"))
        .andExpect(status().isCreated());
  }

  @Test
  public void whenCreateBookWithoutRequiredParam_thenNoBookIsCreated() throws Exception {
    String url = ("/api/books");
    mvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\":0,\"genre\":\"Fantasia\","
            + "\"author\":\"J.K. Rowling\",\"image\":null,\"title\":\""
            + "Harry Potter y la piedra filosofal\",\"publisher\":\"Penguin\",\"year\":"
            + "\"1998\",\"pages\":310,\"isbn\":\"9788700631625\"}"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenDeleteBook_thenBookIsDeleted() throws Exception {
    when(mockBookRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(book));
    String url = ("/api/books/1");
    mvc.perform(delete(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(mockBookRepository, times(1)).deleteById(1L);
  }

  @Test
  public void whenDeleteUserNotFound_thenThrow4XXError() throws Exception {
    String url = ("/api/books/1");
    mvc.perform(delete(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(mockBookRepository, times(0)).deleteById(1L);
  }

  @Test
  public void whenUpdateBook_thenBookIsUpdated() throws Exception {
    when(mockBookRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(book));

    String url = ("/api/books/1");
    mvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\":1,\"genre\":\"Fantasia\","
            + "\"author\":\"J.K. Rowling\",\"image\":\"shorturl.at/aCFR8\",\"title\":\""
            + "Harry Potter y la piedra filosofal\",\"publisher\":\"Penguin\",\"year\":"
            + "\"1998\",\"pages\":310,\"isbn\":\"123456789\"}")).andDo(print())
        .andExpect(status().isOk());
  }
}
