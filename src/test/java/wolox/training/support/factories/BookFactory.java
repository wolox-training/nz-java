package wolox.training.support.factories;

import com.github.javafaker.Faker;
import wolox.training.models.Book;

public class BookFactory {

  Faker faker = new Faker();

  private String genre = faker.book().genre();
  private String author = faker.book().author();
  private String image = faker.avatar().image();
  private String title = faker.book().title();
  private String publisher = faker.book().publisher();
  private String year = Integer.toString(faker.number().numberBetween(1900,2020));
  private Integer pages = faker.number().numberBetween(100,5000);
  private String isbn = faker.number().digits(10);

  public Book build() {
    return new Book(
        this.genre,
        this.author,
        this.image,
        this.title,
        this.publisher,
        this.year,
        this.pages,
        this.isbn);
  };

  public BookFactory title(String title) {
    this.title = title;
    return this;
  }
}
