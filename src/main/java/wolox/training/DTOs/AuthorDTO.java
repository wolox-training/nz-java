package wolox.training.DTOs;

import lombok.Data;

@Data
public class AuthorDTO implements DTO {
  private String url;
  private String name;


  public AuthorDTO(String name) {
    this.setName(name);
  }
}
