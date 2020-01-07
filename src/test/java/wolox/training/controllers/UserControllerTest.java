package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import wolox.training.repositories.UserRepository;
import wolox.training.support.factories.BookFactory;
import wolox.training.support.factories.UserFactory;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@ContextConfiguration(classes = {UserController.class})
public class UserControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  UserRepository mockUserRepository;

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
  public void whenFindByIdWhichExists_thenUserIsReturned() throws Exception {
    Mockito.when(mockUserRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
    String url = ("api/users/1");
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
}
