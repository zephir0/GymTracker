package com.gymtracker.exercise;

import com.gymtracker.user.User;
import com.gymtracker.user.UserRoles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "adminCreated", expression = "java(isAdmin(user))")
    Exercise toEntity(ExerciseDto exerciseDto,
                      User user);

    default boolean isAdmin(User user) {
        return user.getUserRole().equals(UserRoles.ADMIN);
    }
}

