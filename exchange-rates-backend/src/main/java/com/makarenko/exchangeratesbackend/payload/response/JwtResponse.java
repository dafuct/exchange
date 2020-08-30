package com.makarenko.exchangeratesbackend.payload.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

  private String accessToken;
  private Long id;
  private String username;
  private String email;
  private String phone;
  private List<String> roles;

  public JwtResponse(String accessToken, Long id, String username, String email, String phone,
      List<String> roles) {
    this.accessToken = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.phone = phone;
    this.roles = roles;
  }
}
