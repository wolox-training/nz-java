package wolox.training.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.support.factories.BookFactory;
import wolox.training.support.factories.UserFactory;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@ContextConfiguration(classes = {UserController.class})
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  MockMvc mvc;

  @MockBean
  UserRepository mockUserRepository;

  @MockBean
  BookRepository mockBookRepository;

  private User user;
  private Book book;

  @Before
  public void setUp() {

    user = new UserFactory()
        .name("Nicolas Zarewsky")
        .username("nicozare")
        .birthDate(LocalDate.parse("1995-01-30"))
        .build();
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
    user.addBook(book);
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenFindByIdWhichExists_thenUserIsReturned() throws Exception {
    when(mockUserRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
    String url = ("/api/users/1");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\"id\": 0,\"username\":\"nicozare\",\"name\":\"Nicolas Zarewsky\","
                + "\"birthDate\":\"1995-01-30\",\"books\":[{\"id\":0,\"genre\":\"Fantasia\","
                + "\"author\":\"J.K. Rowling\",\"image\":\"shorturl.at/aCFR8\",\"title\":\""
                + "Harry Potter y la piedra filosofal\",\"publisher\":\"Penguin\",\"year\":"
                + "\"1998\",\"pages\":310,\"isbn\":\"9788700631625\"}]}"
        ));
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenFindByIdWhichDoesNotExists_thenNoUserIsReturned() throws Exception {
    String url = ("/api/users/1");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenCreateUser_thenUserIsCreated() throws Exception {
    String url = ("/api/users");
    mvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
          .content("{\"name\": \"test\", \"username\": \"test1\", "
                   + "\"birthDate\": \"1999-03-27\"}"))
        .andExpect(status().isCreated());
  }

  @Test
  public void whenCreateUserWithoutRequiredParam_thenNoUserIsCreated() throws Exception {
    String url = ("/api/users");
    mvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": null, \"username\": \"test1\", "
            + "\"birthDate\": \"1999-03-27\"}"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenDeleteUser_thenUserIsDeleted() throws Exception {
    User simpleUser = new UserFactory().build();
    when(mockUserRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(simpleUser));
    String url = ("/api/users/1");
    mvc.perform(delete(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(mockUserRepository, times(1)).deleteById(1L);
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenDeleteUserNotFound_thenThrow4XXError() throws Exception {
    String url = ("/api/users/1");
    mvc.perform(delete(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(mockUserRepository, times(0)).deleteById(1L);
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenUpdateUser_thenUserIsUpdated() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(user));
    String url = ("/api/users/1");
    mvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\": 1,\"name\": \"Nicolas Zarewsky\", \"username\": \"test1\", "
            + "\"birthDate\": \"1995-01-30\",\"books\":[{\"id\":0,\"genre\":\"Fantasia\","
            + "\"author\":\"J.K. Rowling\",\"image\":\"shorturl.at/aCFR8\",\"title\":\""
            + "Harry Potter y la piedra filosofal\",\"publisher\":\"Penguin\",\"year\":"
            + "\"1998\",\"pages\":310,\"isbn\":\"9788700631625\"}]}"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "nico", password = "1234567890")
  public void whenAddingANewBook_thenBookIsAddedToUser() throws Exception {
    Book harryPotter2 = new BookFactory()
        .author("J.K. Rowling")
        .title("Harry Potter y la camara de los secretos")
        .genre("Fantasia")
        .image("shorturl.at/aCFR7")
        .isbn("9788700631225")
        .pages(500)
        .publisher("Penguin")
        .year("1999")
        .build();

    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockBookRepository.findById(1L))
        .thenReturn(Optional.ofNullable(harryPotter2));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    String url = ("/api/users/1/books/1");

    mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.books", hasSize(2)))
        .andExpect(jsonPath("$.books[0].title").value("Harry Potter y"
            + " la piedra filosofal"))
        .andExpect(jsonPath("$.books[1].title").value("Harry Potter y"
            + " la camara de los secretos"));
  }

  @Test
  @WithMockUser(username = "nico", password = "1234567890")
  public void whenAddingExistingBook_thenExceptionIsThrown() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockBookRepository.findById(1L))
        .thenReturn(Optional.ofNullable(book));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    String url = ("/api/users/1/books/1");

    mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "nico", password = "1234567890")
  public void whenRemovingBook_thenBookIsRemoved() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockBookRepository.findById(1L))
        .thenReturn(Optional.ofNullable(book));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    String url = ("/api/users/1/books/1");

    mvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.books", hasSize(0)));
  }

  @Test
  @WithMockUser(username = "nico", password = "1234567890")
  public void whenRemovingANotPersistedBook_thenExceptionIsThrown() throws Exception {
    when(mockUserRepository.findById(1L))
        .thenReturn(Optional.ofNullable(user));
    when(mockUserRepository.save(user))
        .thenReturn(user);

    String url = ("/api/users/1/books/100");

    mvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().is4xxClientError());
  }
}
