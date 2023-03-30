package com.gymtracker.diary_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryLogRepository extends JpaRepository<DiaryLog, Long> {
    List<DiaryLog> findAllByExerciseIdAndGymDiaryId(Long exerciseId,
                                                    Long gymDiaryId);

    List<DiaryLog> findAllByGymDiaryId(Long id);

    List<DiaryLog> findAllByExerciseId(Long id);
}
