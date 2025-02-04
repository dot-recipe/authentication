package com.noah.dotrecipe.authentication.service;

import com.noah.dotrecipe.authentication.dto.RegisterDto;
import com.noah.dotrecipe.authentication.entities.User;
import com.noah.dotrecipe.authentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  
  @Transactional
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }
  
  @Transactional
  public User findById(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException());
  }
  
  @Transactional
  public User createUser(RegisterDto registerDto) {
    if (existsByUsername(registerDto.getUsername())) {
      throw new RuntimeException();
    }
    User user = User.builder()
        .username(registerDto.getUsername())
        .email(registerDto.getEmail())
        .password(
            passwordEncoder.encode(registerDto.getPassword())
        )
        .createdAt(LocalDateTime.now())
        .build();
    return userRepository.save(user);
  }
}
