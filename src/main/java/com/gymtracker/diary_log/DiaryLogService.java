package com.gymtracker.diary_log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryLogService {

    private final DiaryLogRepository diaryLogRepository;
    private final DiaryLogMapper diaryLogMapper;

    public void createDiaryLog(DiaryLogDto diaryLogDto) {
        DiaryLog diaryLog = diaryLogMapper.toEntity(diaryLogDto);
        diaryLogRepository.save(diaryLog);
    }
}
