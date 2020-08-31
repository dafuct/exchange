package com.makarenko.exchangeratesbackend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.makarenko.exchangeratesbackend.exceptions.UserException;
import com.makarenko.exchangeratesbackend.models.User;
import com.makarenko.exchangeratesbackend.repository.UserRepository;
import com.makarenko.exchangeratesbackend.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  private static User[] users;

  @BeforeAll
  static void beforeAll() {
    users = new User[]{new User(
        "Anna", "anna@gmail", "+380000000000", "anna")
    };
  }

  @Test
  void testSavedUserSuccessFully() {
    when(userRepository.save(users[0])).thenReturn(users[0]);

    User savedUser = userService.save(users[0]);
    assertThat(savedUser).isNotNull();

    verify(userRepository).save(any(User.class));
  }

  @Test
  void testThrowErrorWhenSavedUserWithExistingUsername() {
    when(userRepository.existsByUsername(users[0].getUsername())).thenReturn(true);

    assertThrows(UserException.class, () -> userService.save(users[0]),
        "User with username: Anna is exist");
  }

  @Test
  void testFindByUserName() {
    when(userRepository.findByUsername(users[0].getUsername())).thenReturn(users[0]);

    User expectedUser = userService.findByUsername(users[0].getUsername());
    assertThat(expectedUser).isNotNull();

    verify(userRepository).findByUsername(expectedUser.getUsername());
  }

  @Test
  void testExistByUserName() {
    when(userRepository.existsByUsername(users[0].getUsername())).thenReturn(true);

    Boolean existsByUsername = userService.existsByUsername(users[0].getUsername());
    assertTrue(existsByUsername);

    verify(userRepository).existsByUsername(users[0].getUsername());
  }
}
