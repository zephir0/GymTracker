package com.gymtracker.diary_log;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/diary-log")
public class DiaryLogController {
    private final DiaryLogService diaryLogService;

    @PostMapping
    public ResponseEntity<String> createDiaryLog(@RequestBody @Valid DiaryLogDto diaryLogDto) {
        diaryLogService.createDiaryLog(diaryLogDto);
        return new ResponseEntity<>("Diary log has been created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DiaryLog>> getGymDiaryLogsForGymDiary(@PathVariable Long id) {
        return new ResponseEntity<>(diaryLogService.getGymDiaryLogsForGymDiary(id), HttpStatus.OK);
    }

}
