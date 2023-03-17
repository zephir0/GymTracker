package com.gymtracker.user;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserRegisterMapper {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    LocalDateTime localDateTime = LocalDateTime.now();

    @Mapping(target = "creationDate", expression = "java(localDateTime.now())")
    @Mapping(constant = "USER", target = "userRole")
    User toEntity(UserRegisterDto userRegisterDto);


    @AfterMapping
    default void encodePassword(UserRegisterDto userRegisterDto,
                                @MappingTarget User user) {
        if (userRegisterDto.password() != null && !userRegisterDto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRegisterDto.password()));
        }
    }
}
