package com.noah.dotrecipe.authentication.repository;

import com.noah.dotrecipe.authentication.entities.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  void deleteUserByUsername(String username);

  Optional<User> findById(@NonNull UUID id);
}
