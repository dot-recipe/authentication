package com.noah.dotrecipe.authentication.integration.service;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.noah.dotrecipe.authentication.entities.User;
import com.noah.dotrecipe.authentication.exceptions.UserNameNotUniqueException;
import com.noah.dotrecipe.authentication.integration.config.PostgresTest;
import com.noah.dotrecipe.authentication.service.UserService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UserServiceTests extends PostgresTest {

  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("Test create user")
  void testCreateUser() {
    userService.createUser(User.builder()
        .username(USERNAME + "1")
        .email(EMAIL + "1")
        .password(PASSWORD)
        .createdAt(LocalDateTime.now())
        .build());
    assertTrue(userService.userExists(USERNAME + "1"));
  }

  @Test
  @DisplayName("Duplicate username should throw error")
  void testDuplicateUsername() {
    User user = User.builder()
        .username(USERNAME)
        .password(PASSWORD)
        .createdAt(LocalDateTime.now())
        .build();
    assertThrows(UserNameNotUniqueException.class, () -> userService.createUser(user));
  }

}
