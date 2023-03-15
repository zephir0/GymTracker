package com.gymtracker.exercise;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    Exercise toEntity(ExerciseDto exerciseDto);
}
