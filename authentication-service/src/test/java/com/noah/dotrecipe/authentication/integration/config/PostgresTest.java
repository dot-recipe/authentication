package com.noah.dotrecipe.authentication.integration.config;

import com.noah.dotrecipe.authentication.entities.User;
import com.noah.dotrecipe.authentication.repository.UserRepository;
import com.noah.dotrecipe.authentication.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class PostgresTest {

  public static final String USERNAME = "USER";
  public static final String PASSWORD = "PASSWORD";
  public static final String EMAIL = "TEST@TEST.com";
  
  @Container
  @ServiceConnection
  public static PostgreSQLContainer postgreSQLContainer 
      = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
  
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  @BeforeAll
  public static void beforeAll() {
    postgreSQLContainer.start();
  }

  @BeforeEach
  public void beforeEach() {
    userService.createUser(User.builder()
        .username(USERNAME)
        .email(EMAIL)
        .password(PASSWORD)
        .createdAt(LocalDateTime.now())
        .build());
  }

  @AfterEach
  public void afterEach() {
    userRepository.deleteAll();
  }
}
