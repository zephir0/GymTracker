package com.gymtracker.diary_log;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiaryLogMapper {
    @Mapping(source = "exerciseId", target = "exercise.id")
    @Mapping(source = "gymDiaryId", target = "gymDiary.id")
    DiaryLog toEntity(DiaryLogDto diaryLogDto);
}
