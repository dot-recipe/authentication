package com.noah.dotrecipe.authentication.service;

import com.noah.dotrecipe.authentication.dto.RegisterDto;
import com.noah.dotrecipe.authentication.entities.User;
import com.noah.dotrecipe.authentication.exceptions.UserNameNotUniqueException;
import com.noah.dotrecipe.authentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsManager {
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void createUser(UserDetails userDetails) {
    if (!(userDetails instanceof User user)) {
      throw new IllegalArgumentException("User must be an instance of User class");
    }
    if (userExists(user.getUsername())) {
      throw new UserNameNotUniqueException(
          MessageFormat.format("Username {0} already exists", user.getUsername())
      );
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
  }

  @Override
  public void updateUser(UserDetails user) {
    // TODO
  }

  @Override
  public void deleteUser(String username) {
    // TODO
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    // TODO
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            MessageFormat.format("Username {0} not found", username)
        ));
  }

  public User findById(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException(
            MessageFormat.format("ID {0} not found for any user", id)
        ));
  }
}
