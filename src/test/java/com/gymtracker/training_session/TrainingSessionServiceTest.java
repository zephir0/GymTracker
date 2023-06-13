//package com.gymtracker.training_session;
//
//import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
//import com.gymtracker.user.UserService;
//import com.gymtracker.user.entity.User;
//import com.gymtracker.user.entity.UserRoles;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class TrainingSessionServiceTest {
//
//    private final Long trainingSessionId = 1L;
//    private TrainingSessionServiceImpl trainingSessionService;
//    @Mock
//    private TrainingSessionRepository trainingSessionRepository;
//    @Mock
//    private TrainingSessionMapper trainingSessionMapper;
//    @Mock
//    private UserService userService;
//    private TrainingSessionDto trainingSessionDto;
//    private TrainingSession trainingSession;
//    private User user;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        trainingSessionService = new TrainingSessionServiceImpl(trainingSessionRepository, trainingSessionMapper, userService);
//
//        trainingSessionDto = new TrainingSessionDto("testName");
//        trainingSession = mock(TrainingSession.class);
//        trainingSession.setId(trainingSessionId);
//        user = new User();
//        user.setUserRole(UserRoles.USER);
//        user.setId(1L);
//    }
//
//    @Test
//    public void testCreateTrainingSession_Success() {
//        when(userService.getLoggedUser()).thenReturn(user);
//        when(trainingSessionMapper.toEntity(trainingSessionDto, user)).thenReturn(trainingSession);
//
//        assertDoesNotThrow(() -> trainingSessionService.createTrainingSession(trainingSessionDto));
//        verify(trainingSessionRepository, times(1)).save(trainingSession);
//    }
//
//    @Test
//    public void testEditTrainingSession_TrainingSessionNotFound() {
//        when(trainingSessionRepository.findById(trainingSessionId)).thenReturn(Optional.empty());
//
//        assertThrows(TrainingSessionNotFoundException.class, () -> trainingSessionService.editTrainingSession(trainingSessionId, trainingSessionDto));
//    }
//
//    @Test
//    public void testEditTrainingSession_Success() {
//        when(trainingSessionRepository.findById(trainingSessionId)).thenReturn(Optional.of(trainingSession));
//        when(userService.getLoggedUser()).thenReturn(user);
//        when(trainingSession.getUser()).thenReturn(user);
//
//        assertDoesNotThrow(() -> trainingSessionService.editTrainingSession(trainingSessionId, trainingSessionDto));
//        verify(trainingSessionRepository, times(1)).save(any(TrainingSession.class));
//    }
//
//    @Test
//    public void testDeleteTrainingSession_TrainingSessionNotFound() {
//        when(trainingSessionRepository.findById(trainingSessionId)).thenReturn(Optional.empty());
//
//        assertThrows(TrainingSessionNotFoundException.class, () -> trainingSessionService.deleteTrainingSession(trainingSessionId));
//    }
//
//    @Test
//    public void testDeleteTrainingSession_Success() {
//        when(trainingSessionRepository.findById(trainingSessionId)).thenReturn(Optional.of(trainingSession));
//        when(userService.getLoggedUser()).thenReturn(user);
//        when(trainingSession.getUser()).thenReturn(user);
//
//        assertDoesNotThrow(() -> trainingSessionService.deleteTrainingSession(trainingSessionId));
//        verify(trainingSessionRepository, times(1)).deleteById(trainingSessionId);
//    }
//
//    @Test
//    public void testGetTrainingSessionDtoById_TrainingSessionNotFound() {
//        when(trainingSessionRepository.findById(trainingSessionId)).thenReturn(Optional.empty());
//
//        assertThrows(TrainingSessionNotFoundException.class, () -> trainingSessionService.getTrainingSessionDtoById(trainingSessionId));
//    }
//
//    @Test
//    public void testGetTrainingSessionDtoById_Success() {
//        when(trainingSessionRepository.findById(trainingSessionId)).thenReturn(Optional.of(trainingSession));
//        when(userService.getLoggedUser()).thenReturn(user);
//        TrainingSessionResponseDto responseDto = new TrainingSessionResponseDto(1L, LocalDateTime.now(), "trainingName", 100);
//        when(trainingSessionMapper.toDto(trainingSession)).thenReturn(responseDto);
//        when(trainingSession.getUser()).thenReturn(user);
//        TrainingSessionResponseDto actualResponseDto = trainingSessionService.getTrainingSessionDtoById(trainingSessionId);
//        assertEquals(responseDto, actualResponseDto);
//    }
//
//    @Test
//    public void testGetAllTrainingSessionsForLoggedUser_Success() {
//        when(userService.getLoggedUser()).thenReturn(user);
//        List<TrainingSession> trainingSessions = new ArrayList<>();
//        when(trainingSessionRepository.findAllByUser(user)).thenReturn(trainingSessions);
//        TrainingSessionResponseDto responseDto = new TrainingSessionResponseDto(1L, LocalDateTime.now(), "trainingName", 100);
//        when(trainingSessionMapper.toDto(any())).thenReturn(responseDto);
//
//        List<TrainingSessionResponseDto> actualResponseDto = trainingSessionService.getAllTrainingSessionsForLoggedUser();
//        assertEquals(trainingSessions.size(), actualResponseDto.size());
//    }
//
//}
