package com.gymtracker.gym_diary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/gym-diary")
public class GymDiaryController {
    private final GymDiaryService gymDiaryService;

    @PostMapping
    ResponseEntity<String> createDiary(@RequestBody @Valid GymDiaryDto gymDiaryDto) {
        gymDiaryService.createDiary(gymDiaryDto);
        return new ResponseEntity<>("Gym diary has been created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteDiary(@PathVariable Long id) {
        gymDiaryService.deleteDiary(id);
        return new ResponseEntity<>("Gym diare has been deleted", HttpStatus.OK);
    }

    @GetMapping()
    ResponseEntity<List<GymDiary>> getAllGymDiariesForLoggedUser() {
        List<GymDiary> gymDiaries = gymDiaryService.getAllGymDiariesForLoggedUser();
        return new ResponseEntity<>(gymDiaries, HttpStatus.OK);
    }

}
