package com.gymtracker.diary_log;

import com.gymtracker.gym_diary.GymDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryLogService {

    private final DiaryLogRepository diaryLogRepository;
    private final DiaryLogMapper diaryLogMapper;
    private final GymDiaryService gymDiaryService;

    public void createDiaryLog(DiaryLogDto diaryLogDto) {
        DiaryLog diaryLog = diaryLogMapper.toEntity(diaryLogDto);
        diaryLogRepository.save(diaryLog);
    }

    public List<DiaryLog> getGymDiaryLogsForGymDiary(Long id) {
        List<DiaryLog> diaryLogs = gymDiaryService.findById(id).getDiaryLogs();
        if (diaryLogs.isEmpty()) {
            throw new GymDiaryLogsNotFoundException("There is no logs in that diary");
        } else return diaryLogs;
    }
}
