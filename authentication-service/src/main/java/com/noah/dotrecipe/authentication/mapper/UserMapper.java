package com.noah.dotrecipe.authentication.mapper;

import com.noah.dotrecipe.authentication.dto.RegisterDto;
import com.noah.dotrecipe.authentication.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring"
)
public interface UserMapper {
  
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  User toUser(RegisterDto registerDto);
}
