package com.makarenko.exchangeratesbackend.payload.request;


import java.util.Set;

import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 13)
  @Email
  private String phone;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  private Set<String> role;
}

