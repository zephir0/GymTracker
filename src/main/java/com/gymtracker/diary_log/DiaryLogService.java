package com.gymtracker.diary_log;

import com.gymtracker.exercise.ExerciseService;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.gym_diary.GymDiary;
import com.gymtracker.gym_diary.GymDiaryService;
import com.gymtracker.gym_diary.exception.GymDiaryNotFoundException;
import com.gymtracker.gym_diary.exception.UnauthorizedDiaryAccessException;
import com.gymtracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryLogService {

    private final DiaryLogRepository diaryLogRepository;
    private final DiaryLogMapper diaryLogMapper;
    private final GymDiaryService gymDiaryService;
    private final ExerciseService exerciseService;
    private final UserService userService;

    public void createDiaryLog(DiaryLogDto diaryLogDto) {

        if (exerciseService.existById(diaryLogDto.gymDiaryId())) {
            throw new ExerciseNotFoundException("Exercise with that id doesn't exist");
        }

        GymDiary gymDiary = gymDiaryService.findById(diaryLogDto.gymDiaryId());

        checkAuthorization(gymDiary);

        DiaryLog diaryLog = diaryLogMapper.toEntity(diaryLogDto);
        diaryLogRepository.save(diaryLog);
    }

    public void deleteDiaryLog(Long id) {
        diaryLogRepository.findById(id).ifPresentOrElse(diaryLog -> {
            checkAuthorization(diaryLog.getGymDiary());
            diaryLogRepository.deleteById(diaryLog.getId());
        }, () -> {
            throw new GymDiaryNotFoundException("Gym diary log doesn't exist");
        });
    }

    public void editDiaryLog(Long id,
                             DiaryLogDto diaryLogDto) {
        diaryLogRepository.findById(id).ifPresentOrElse(diaryLog -> {
            checkAuthorization(diaryLog.getGymDiary());
            diaryLog.setReps(diaryLogDto.reps());
            diaryLog.setWeight(diaryLogDto.weight());
            diaryLog.setPersonalNotes(diaryLogDto.personalNotes());
            diaryLogRepository.save(diaryLog);
        }, () -> {
            throw new GymDiaryNotFoundException("Gym diary log doesn't exist");
        });
    }


    public List<DiaryLog> getGymDiaryLogsForGymDiary(Long id) {
        GymDiary gymDiary = gymDiaryService.findById(id);

        checkAuthorization(gymDiary);

        List<DiaryLog> diaryLogs = gymDiary.getDiaryLogs();

        if (diaryLogs.isEmpty()) {
            throw new GymDiaryLogsNotFoundException("There is no logs in that diary");
        } else return diaryLogs;

    }

    private void checkAuthorization(GymDiary gymDiary) {
        if (!gymDiaryService.isDiaryOwnerOrAdmin(gymDiary, userService.getLoggedUser())) {
            throw new UnauthorizedDiaryAccessException("You are not a diary creator or admin");
        }
    }
}
