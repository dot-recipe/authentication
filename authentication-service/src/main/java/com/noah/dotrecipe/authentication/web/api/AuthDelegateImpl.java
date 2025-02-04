package com.noah.dotrecipe.authentication.web.api;

import com.noah.dotrecipe.authentication.api.AuthApiDelegate;
import com.noah.dotrecipe.authentication.config.TokenGenerator;
import com.noah.dotrecipe.authentication.dto.JwtTokenDto;
import com.noah.dotrecipe.authentication.dto.LoginDto;
import com.noah.dotrecipe.authentication.dto.RegisterDto;
import com.noah.dotrecipe.authentication.entities.User;
import com.noah.dotrecipe.authentication.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthDelegateImpl implements AuthApiDelegate {
  
  private final UserService userService;
  private final TokenGenerator tokenGenerator;
  private final DaoAuthenticationProvider daoAuthenticationProvider;
  private final JwtAuthenticationProvider refreshTokenAuthProvider;
  
  @Override
  public ResponseEntity<JwtTokenDto> register(RegisterDto registerDto) {
    if (isUserExists(registerDto.getUsername())) {  
      return ResponseEntity.badRequest().build();
    }
    User user = userService.createUser(registerDto);
    Authentication authentication =
        UsernamePasswordAuthenticationToken.authenticated(
            user, registerDto.getPassword(), user.getAuthorities()
        );
    return ResponseEntity.ok(tokenGenerator.createToken(authentication));
  }
  
  @Override
  public ResponseEntity<JwtTokenDto> login(LoginDto loginDto) {
    if (!isUserExists(loginDto.getUsername())) {
      return ResponseEntity.badRequest().build();
    }
    Authentication authentication =
        daoAuthenticationProvider.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                loginDto.getUsername(), loginDto.getPassword()
            )
        );
    return ResponseEntity.ok(tokenGenerator.createToken(authentication)); 
  }
  
  @Override
  public ResponseEntity<JwtTokenDto> token(JwtTokenDto jwtTokenDto) {
    Authentication authentication =
        refreshTokenAuthProvider.authenticate(
            new BearerTokenAuthenticationToken(jwtTokenDto.getRefreshToken())
        );
    Jwt jwt = (Jwt) authentication.getCredentials();
    User user = userService.findById(UUID.fromString(jwt.getSubject()));
    if (!user.isAccountNonLocked()) {
      //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is locked");
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(tokenGenerator.createToken(authentication));
  }
  
  private boolean isUserExists(String username) {
    return userService.existsByUsername(username);
  }
}
