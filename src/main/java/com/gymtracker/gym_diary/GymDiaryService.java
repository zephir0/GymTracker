package com.gymtracker.gym_diary;

import com.gymtracker.gym_diary.exception.GymDiaryNotFoundException;
import com.gymtracker.gym_diary.exception.UnauthorizedDiaryAccessException;
import com.gymtracker.user.User;
import com.gymtracker.user.UserRoles;
import com.gymtracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GymDiaryService {

    private final GymDiaryRepository gymDiaryRepository;
    private final GymDiaryMapper gymDiaryMapper;
    private final UserService userService;

    public void createDiary(GymDiaryDto gymDiaryDto) {

        GymDiary gymDiary = gymDiaryMapper.toEntity(gymDiaryDto, userService.getLoggedUser());
        gymDiary.setTrainingDate(LocalDateTime.now());
        gymDiaryRepository.save(gymDiary);
    }

    public void editDiary(Long id,
                            GymDiaryDto gymDiaryDto) {
        gymDiaryRepository.findById(id).ifPresentOrElse(gymDiary -> {
            if (isDiaryOwnerOrAdmin(gymDiary, userService.getLoggedUser())) {
                gymDiary.setTrainingName(gymDiaryDto.trainingName());
                gymDiaryRepository.save(gymDiary);
            } else throw new UnauthorizedDiaryAccessException("You are not a gym diary creator or admin.");
        }, () -> {
            throw new GymDiaryNotFoundException("Gym diary doesn't exist in database");
        });
    }

    public void deleteDiary(Long id) {
        gymDiaryRepository.findById(id).ifPresentOrElse(gymDiary -> {
            if (isDiaryOwnerOrAdmin(gymDiary, userService.getLoggedUser())) {
                gymDiaryRepository.deleteById(id);
            } else throw new UnauthorizedDiaryAccessException("You are not a gym diary creator or admin.");
        }, () -> {
            throw new GymDiaryNotFoundException("Gym diary doesn't exist in database");
        });
    }

    public GymDiary findById(Long id) {
        return gymDiaryRepository.findById(id)
                .orElseThrow(() -> new GymDiaryNotFoundException("Gym diary was not found."));
    }


    public boolean isDiaryOwnerOrAdmin(GymDiary gymDiary,
                                       User user) {
        return (user.getUserRole().equals(UserRoles.ADMIN) || gymDiary.getUser().equals(user));
    }


    public List<GymDiary> getAllGymDiariesForLoggedUser() {
        return gymDiaryRepository.findAllByUser(userService.getLoggedUser());
    }


}
