package com.gymtracker.exercise;

import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(source = "exerciseDto.id", target = "id")
    @Mapping(target = "adminCreated", expression = "java(isAdmin(user))")
    Exercise toEntity(ExerciseDto exerciseDto,
                      User user);

    ExerciseResponseDto toDto(Exercise exercise);


    default boolean isAdmin(User user) {
        return user.getUserRole().equals(UserRoles.ADMIN);
    }
}

