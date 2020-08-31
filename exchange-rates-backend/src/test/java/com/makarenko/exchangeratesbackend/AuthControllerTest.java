package com.makarenko.exchangeratesbackend;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarenko.exchangeratesbackend.config.AppConfiguration;
import com.makarenko.exchangeratesbackend.models.User;
import com.makarenko.exchangeratesbackend.payload.request.LoginRequest;
import com.makarenko.exchangeratesbackend.repository.UserRepository;
import com.makarenko.exchangeratesbackend.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Autowired(required = false)
  private ObjectMapper objectMapper;

  private static User[] users;

  @BeforeAll
  static void beforeAll() {
    users = new User[]{new User(
        "Anna", "anna@gmail", "+380000000000", "anna")
    };
  }

  @Test
  void shouldSavedUser() throws Exception {
    when(userRepository.save(users[0])).thenReturn(users[0]);

    this.mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(users[0])))
        .andExpect(status().isOk());
  }

  @Test
  void shouldLoginUser() throws Exception {
    when(userRepository.findByUsername(users[0].getUsername())).thenReturn(users[0]);
    User expectedUser = userService.findByUsername(users[0].getUsername());
    LoginRequest loginRequest = new LoginRequest(expectedUser.getUsername(),
        expectedUser.getPassword());

    this.mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void shouldPublicContent() throws Exception {
    mockMvc.perform(get("/api/content/all"))
        .andExpect(content().string("Public Content."));
  }
}
