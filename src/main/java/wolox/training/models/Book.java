package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.base.Preconditions;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import wolox.training.constants.PreconditionsConstants;

@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column()
  private String genre;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false)
  private String image;

  @Column()
  private String title;

  @Column(nullable = false)
  private String publisher;

  @Column(nullable = false)
  private String year;

  @Column(nullable = false)
  private Integer pages;

  @Column(nullable = false)
  private String isbn;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "users_id")
  @JsonBackReference
  @Getter @Setter
  private User users;

  public Book(String genre, String author, String image, String title, String publisher,
      String year, Integer pages, String isbn) {
    this.setGenre(genre);
    this.setAuthor(author);
    this.setImage(image);
    this.setTitle(title);
    this.setPublisher(publisher);
    this.setYear(year);
    this.setPages(pages);
    this.setIsbn(isbn);
  }

  public Book(String genre, String author, String title, String publisher,
      String year, Integer pages, String isbn) {
    this.setGenre(genre);
    this.setAuthor(author);
    this.setTitle(title);
    this.setPublisher(publisher);
    this.setYear(year);
    this.setPages(pages);
    this.setIsbn(isbn);
  }

  public Book(){};

  public long getId() {
    return id;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = Preconditions.checkNotNull(author,PreconditionsConstants.NOT_NULL_MESSAGE, "author");
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = Preconditions.checkNotNull(title,PreconditionsConstants.NOT_NULL_MESSAGE, "title");
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = Preconditions.checkNotNull(publisher,PreconditionsConstants.NOT_NULL_MESSAGE, "publisher");
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = Preconditions.checkNotNull(year,PreconditionsConstants.NOT_NULL_MESSAGE, "year");
  }

  public Integer getPages() {
    return pages;
  }

  public void setPages(Integer pages) {
    this.pages = Preconditions.checkNotNull(pages,PreconditionsConstants.NOT_NULL_MESSAGE, "pages");
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = Preconditions.checkNotNull(isbn,PreconditionsConstants.NOT_NULL_MESSAGE, "isbn");
  }
}