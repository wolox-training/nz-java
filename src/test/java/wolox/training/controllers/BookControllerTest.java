package wolox.training.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.DTOs.AuthorDTO;
import wolox.training.DTOs.BookDTO;
import wolox.training.DTOs.PublisherDTO;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.services.third_party.OpenLibraryService;
import wolox.training.support.factories.BookFactory;
import wolox.training.support.factories.UserFactory;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(value = BookController.class)
@ContextConfiguration(classes = {BookController.class,
    OpenLibraryService.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class BookControllerTest {
  @Autowired
  MockMvc mvc;

  @MockBean
  BookRepository mockBookRepository;

  @MockBean
  OpenLibraryService openLibraryService;

  private Book book;
  private BookDTO bookDTO;

  @Before
  public void setUp() {
    AuthorDTO nico = new AuthorDTO();
    nico.setName("Nicolas Zarewsky");

    AuthorDTO rodri = new AuthorDTO();
    rodri.setName("Rodrigo Francou");

    AuthorDTO jime = new AuthorDTO();
    jime.setName("Jimena Rosello");

    AuthorDTO[] author_list = new AuthorDTO[]{nico, rodri, jime};

    PublisherDTO penguin = new PublisherDTO();
    penguin.setName("Penguin");

    PublisherDTO wales = new PublisherDTO();
    wales.setName("Wales");

    PublisherDTO[] publisher_list = new PublisherDTO[]{ penguin, wales };

    bookDTO = new BookDTO();
    bookDTO.setAuthors(author_list);
    bookDTO.setPublishers(publisher_list);
    bookDTO.setGenre(null);
    bookDTO.setPublishDate("2001");
    bookDTO.setPages(123);
    bookDTO.setTitle("TTTest");
    bookDTO.setSubtitle("TTest");
    bookDTO.setIsbn("123456789");
    bookDTO.setImage("pepe");

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
  @WithMockUser(username = "nicozare", password = "1234567890")
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
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenFindByIdWhichDoesNotExists_thenNoBookIsReturned() throws Exception {
    String url = ("/api/books/1");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenFindAll_thenReturnAllBooks() throws Exception {
    Book book_2 = new BookFactory()
        .title("Harry Potter y la camara de los secretos")
        .build();

    Book book_3 = new BookFactory()
        .title("Harry Potter y el prisionero de azkaban")
        .build();

    List<Book> allBooks = Arrays.asList(new Book[]{book, book_2, book_3});
    Page<Book> pagedResponse = new PageImpl(allBooks);

    when(mockBookRepository.findAll(anyString(),anyString(),anyString(),anyString(),anyString(),
        anyString(),anyInt(),anyString(),
        org.mockito.Matchers.isA(Pageable.class)
    )).thenReturn(pagedResponse);

    String url = ("/api/books");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
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
            + "\"author\":\"J.K. Rowling\",\"image\":null,\"title\":"
            + "null,\"publisher\":\"Penguin\",\"year\":"
            + "\"1998\",\"pages\":310,\"isbn\":\"9788700631625\"}"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
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
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenDeleteUserNotFound_thenThrow4XXError() throws Exception {
    String url = ("/api/books/1");
    mvc.perform(delete(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(mockBookRepository, times(0)).deleteById(1L);
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
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

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenFindByIsbn_andBookInDB_thenBookIsReturned() throws Exception {
    when(mockBookRepository.findByIsbn("9788700631625"))
        .thenReturn(java.util.Optional.ofNullable(book));

    String url = ("/api/books/isbn/9788700631625");
    mvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\"id\":0,\"genre\":\"Fantasia\","
                + "\"author\":\"J.K. Rowling\",\"image\":\"shorturl.at/aCFR8\",\"title\":\""
                + "Harry Potter y la piedra filosofal\",\"publisher\":\"Penguin\",\"year\":"
                + "\"1998\",\"pages\":310,\"isbn\":\"9788700631625\"}"
        ));
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenFindByIsbn_andBookNotInDB_thenBookFromOpenLibraryIsSavedAndReturned() throws Exception {
    when(openLibraryService.bookInfo("123456789"))
        .thenReturn(bookDTO);

    when(mockBookRepository.save(ArgumentMatchers.any(Book.class)))
        .thenReturn(bookDTO.toModel());

    String url = ("/api/books/isbn/123456789");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"id\":0,\"genre\":null,"
                + "\"author\":\"Nicolas Zarewsky, Rodrigo Francou, Jimena Rosello\","
                + "\"image\":\"pepe\",\"title\":\""
                + "TTTest\",\"publisher\":\"Penguin, Wales\",\"year\":"
                + "\"2001\",\"pages\":123,\"isbn\":\"123456789\"}"));
  }

  @Test
  @WithMockUser(username = "nicozare", password = "1234567890")
  public void whenFindByIsbn_andBookNotInDB_andBookNotInOpenLibrary_thenThrow4XXError() throws Exception {
    when(openLibraryService.bookInfo("123456789"))
        .thenThrow(BookNotFoundException.class);

    String url = ("/api/books/isbn/123456789");
    mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }
}
