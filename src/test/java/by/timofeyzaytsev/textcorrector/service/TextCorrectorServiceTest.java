package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.dto.request.CreateCorrectionTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.dto.response.FindCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskLanguage;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus;
import by.timofeyzaytsev.textcorrector.mapper.CorrectionTaskMapper;
import by.timofeyzaytsev.textcorrector.repository.CorrectionTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TextCorrectorServiceTest {

    @Mock
    private CorrectionTaskRepository correctionTaskRepository;

    @Mock
    private CorrectionTaskMapper correctionTaskMapper;

    @InjectMocks
    private TextCorrectorService textCorrectorService;

    @Test
    void createTextCorrection_WhenRequestIsValid_ShouldReturnCreateCorrectionTaskResponse() {
        // Arrange
        CreateCorrectionTaskRequest request =
                new CreateCorrectionTaskRequest("Hello World", CorrectionTaskLanguage.EN);

        CorrectionTask task = CorrectionTask.builder().text("Hello World")
                .language(CorrectionTaskLanguage.EN)
                .status(CorrectionTaskStatus.NEW_TASK)
                .id(UUID.randomUUID())
                .build();

        when(correctionTaskMapper.toCorrectionTask(request)).thenReturn(task);
        when(correctionTaskRepository.save(task)).thenReturn(task);

        UUID expectedTaskId = task.getId();

        // Act
        CreateCorrectionTaskResponse actual = textCorrectorService.createCorrectionTask(request);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.taskId()).isEqualTo(expectedTaskId);

        verify(correctionTaskMapper, times(1)).toCorrectionTask(request);
        verify(correctionTaskRepository, times(1)).save(task);
        verifyNoMoreInteractions(correctionTaskMapper, correctionTaskRepository);
    }

    @Test
    void findCorrectionTask_WhenTaskFinished_ShouldReturnResponseWithTask() {
        // Arrange
        UUID testId = UUID.randomUUID();
        CorrectionTask finishedTask = new CorrectionTask();
        finishedTask.setId(testId);
        finishedTask.setStatus(CorrectionTaskStatus.FINISHED);
        finishedTask.setText("исправленный текст");

        FindCorrectionTaskResponse expectedResponse = new FindCorrectionTaskResponse(
                "исправленный текст",
                CorrectionTaskStatus.FINISHED
        );

        when(correctionTaskRepository.findCorrectionTaskById(testId))
                .thenReturn(Optional.of(finishedTask));
        when(correctionTaskMapper.toFindCorrectionTaskResponse(finishedTask))
                .thenReturn(expectedResponse);

        // Act
        FindCorrectionTaskResponse actualResponse = textCorrectorService.findCorrectionTask(testId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        assertEquals("исправленный текст", actualResponse.text());
        assertEquals(CorrectionTaskStatus.FINISHED, actualResponse.status());

        verify(correctionTaskRepository).findCorrectionTaskById(testId);
        verify(correctionTaskMapper).toFindCorrectionTaskResponse(finishedTask);
        verifyNoMoreInteractions(correctionTaskRepository, correctionTaskMapper);
    }

    @Test
    void findCorrectionTask_WhenTaskNotFinished_ShouldReturnResponseWithNullTask() {
        // Arrange
        UUID testId = UUID.randomUUID();
        CorrectionTask inProgressTask = new CorrectionTask();
        inProgressTask.setId(testId);
        inProgressTask.setStatus(CorrectionTaskStatus.PROCCESSING);
        inProgressTask.setText("текст который не должен быть в ответе");

        FindCorrectionTaskResponse expectedResponse = new FindCorrectionTaskResponse(
                null,
                CorrectionTaskStatus.PROCCESSING
        );

        when(correctionTaskRepository.findCorrectionTaskById(testId))
                .thenReturn(Optional.of(inProgressTask));
        when(correctionTaskMapper.toFindCorrectionTaskResponse(argThat(task ->
                task.getStatus() == CorrectionTaskStatus.PROCCESSING &&
                        task.getText() == null
        ))).thenReturn(expectedResponse);

        // Act
        FindCorrectionTaskResponse actualResponse = textCorrectorService.findCorrectionTask(testId);

        // Assert
        assertNotNull(actualResponse);
        assertNull(actualResponse.text());
        assertEquals(CorrectionTaskStatus.PROCCESSING, actualResponse.status());

        verify(correctionTaskRepository).findCorrectionTaskById(testId);
        verify(correctionTaskMapper).toFindCorrectionTaskResponse(argThat(task -> {
            assertNull(task.getText());
            return true;
        }));
    }

    @Test
    void findCorrectionTask_WhenTaskNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        UUID testId = UUID.randomUUID();
        when(correctionTaskRepository.findCorrectionTaskById(testId))
                .thenReturn(Optional.empty());

        // Act and Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> textCorrectorService.findCorrectionTask(testId)
        );

        assertEquals("Task with id: " + testId + " not found", exception.getMessage());

        verify(correctionTaskRepository).findCorrectionTaskById(testId);
        verify(correctionTaskMapper, never()).toFindCorrectionTaskResponse(any());
    }
}
