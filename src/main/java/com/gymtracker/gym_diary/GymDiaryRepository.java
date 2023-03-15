package com.gymtracker.gym_diary;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymDiaryRepository extends CrudRepository<GymDiary, Long> {
}
