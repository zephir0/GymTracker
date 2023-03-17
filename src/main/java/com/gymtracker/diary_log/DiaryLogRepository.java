package com.gymtracker.diary_log;

import com.gymtracker.gym_diary.GymDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryLogRepository extends JpaRepository<DiaryLog, Long> {
    List<DiaryLog> findAllByGymDiaryId(Long id);
}
