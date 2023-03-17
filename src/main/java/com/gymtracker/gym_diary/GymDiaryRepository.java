package com.gymtracker.gym_diary;

import com.gymtracker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymDiaryRepository extends JpaRepository<GymDiary, Long> {
    List<GymDiary> findAllByUser(User user);
}
