package com.makarenko.exchangeratesbackend.payload.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

  private Long id;
  private String token;
  private String type = "Bearer";
  private String username;
  private String email;
  private String phone;
  private List<String> roles;

  public JwtResponse(Long id, String token, String username, String email, String phone,
      List<String> roles) {
    this.id = id;
    this.token = token;
    this.username = username;
    this.email = email;
    this.phone = phone;
    this.roles = roles;
  }
}
