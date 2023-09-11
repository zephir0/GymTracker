package com.gymtracker.training_session;

import com.gymtracker.training_log.service.TrainingLogService;
import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.training_routine.TrainingRoutineService;
import com.gymtracker.training_routine.exception.UnauthorizedTrainingRoutineAccessException;
import com.gymtracker.training_session.dto.TrainingSessionDto;
import com.gymtracker.training_session.dto.TrainingSessionResponseDto;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.mapper.TrainingSessionMapper;
import com.gymtracker.training_session.service.TrainingSessionServiceImpl;
import com.gymtracker.user.service.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrainingSessionServiceTest {
    @Mock
    private TrainingLogService trainingLogService;
    @Mock
    private TrainingSessionRepository trainingSessionRepository;
    @Mock
    private UserService userService;
    @Mock
    private TrainingSessionMapper trainingSessionMapper;
    @Mock
    private TrainingRoutineService trainingRoutineService;


    @InjectMocks
    private TrainingSessionServiceImpl trainingSessionService;

    private TrainingSession trainingSession;
    private TrainingRoutine trainingRoutine;

    private User user;
    private User unauthorizedUser;
    private TrainingSessionDto trainingSessionDto;

    @Before
    public void setUp() {
        Long trainingRoutineId = 1L;

        user = new User();
        user.setId(1L);

        unauthorizedUser = new User();
        unauthorizedUser.setId(2L);

        trainingRoutine = new TrainingRoutine();
        trainingRoutine.setUser(user);
        trainingRoutine.setId(trainingRoutineId);

        trainingSession = new TrainingSession();
        trainingSession.setTrainingRoutine(trainingRoutine);
        trainingSession.setUser(user);
        trainingSession.setId(1L);

        trainingSessionDto = new TrainingSessionDto(trainingRoutineId, new ArrayList<>());

    }

    @Test
    public void testCreateTrainingSession_Success() {
        // Arrange
        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingSessionMapper.toEntity(trainingRoutine, user)).thenReturn(trainingSession);
        when(trainingSessionRepository.save(trainingSession)).thenReturn(trainingSession);
        when(trainingRoutineService.getTrainingRoutine(any())).thenReturn(trainingRoutine);
        // Act
        trainingSessionService.createTrainingSession(trainingSessionDto);

        // Assert
        verify(trainingSessionRepository, times(1)).save(trainingSession);
        verify(trainingLogService, times(1)).createTrainingLogs(eq(trainingSessionDto.trainingLogDtoList()), any());
    }


    @Test(expected = UnauthorizedTrainingRoutineAccessException.class)
    public void testCreateTrainingSession_Unauthorized() {
        // Arrange
        trainingRoutine.setUser(unauthorizedUser);

        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingSessionMapper.toEntity(trainingRoutine, user)).thenReturn(trainingSession);
        when(trainingRoutineService.getTrainingRoutine(any())).thenReturn(trainingRoutine);


        // Act
        trainingSessionService.createTrainingSession(trainingSessionDto);

        // Assert
        verify(trainingSessionRepository, never()).save(any());
        verify(trainingLogService, never()).createTrainingLogs(any(), anyLong());
    }

    @Test
    public void testDeleteTrainingSession_Success() {
        // Arrange
        user.setUserRole(UserRoles.USER);
        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingSessionRepository.findById(trainingSession.getId())).thenReturn(Optional.of(trainingSession));

        // Act
        trainingSessionService.deleteTrainingSession(trainingSession.getId());

        // Assert
        verify(trainingSessionRepository, times(1)).deleteById(trainingSession.getId());
    }

    @Test(expected = TrainingSessionNotFoundException.class)
    public void testDeleteTrainingSession_NotFound() {
        // Arrange
        when(trainingSessionRepository.findById(trainingSession.getId())).thenReturn(Optional.empty());

        // Act
        trainingSessionService.deleteTrainingSession(trainingSession.getId());
    }

    @Test
    public void testGetTrainingSessionDtoById_Success() {
        // Arrange
        user.setUserRole(UserRoles.USER);
        when(trainingSessionRepository.findById(trainingSession.getId())).thenReturn(Optional.of(trainingSession));
        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingSessionMapper.toDto(trainingSession)).thenReturn(new TrainingSessionResponseDto(1L, "TestRoutineName", LocalDate.now(), 1029L));

        // Act
        trainingSessionService.getTrainingSessionDtoById(trainingSession.getId());

        // Assert
        verify(trainingSessionRepository, times(1)).findById(trainingSession.getId());
        verify(userService, times(2)).getLoggedUser();
        verify(trainingSessionMapper, times(1)).toDto(trainingSession);
    }

    @Test
    public void testGetTrainingSessionById_Success() {
        // Arrange
        when(trainingSessionRepository.findById(trainingSession.getId())).thenReturn(Optional.of(trainingSession));

        // Act
        Optional<TrainingSession> result = trainingSessionService.getTrainingSessionById(trainingSession.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainingSession, result.get());
    }

    @Test(expected = TrainingSessionNotFoundException.class)
    public void testGetTrainingSessionDtoById_NotFound() {
        // Arrange
        when(trainingSessionRepository.findById(trainingSession.getId())).thenReturn(Optional.empty());

        // Act
        trainingSessionService.getTrainingSessionDtoById(trainingSession.getId());
    }


    @Test
    public void testGetAllTrainingSessionsForLoggedUser_Success() {
        // Arrange
        when(userService.getLoggedUser()).thenReturn(user);
        List<TrainingSession> trainingSessions = new ArrayList<>();
        trainingSessions.add(trainingSession);
        when(trainingSessionRepository.findTrainingSessionsByUser(user)).thenReturn(trainingSessions);


        // Act
        List<TrainingSessionResponseDto> result = trainingSessionService.getAllTrainingSessionsForLoggedUser();

        // Assert
        assertNotNull(result);
    }

    @Test(expected = TrainingSessionNotFoundException.class)
    public void testGetAllTrainingSessionsForLoggedUser_NoTrainingSessions() {
        // Arrange
        when(userService.getLoggedUser()).thenReturn(user);

        // Act
        trainingSessionService.getAllTrainingSessionsForLoggedUser();
    }

}
