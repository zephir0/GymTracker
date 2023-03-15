package com.gymtracker.gym_diary;

import com.gymtracker.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface GymDiaryMapper {

    @Mapping(target = "user", source = "user")
    GymDiary toEntity(GymDiaryDto gymDiaryDto,
                      User user);

}
