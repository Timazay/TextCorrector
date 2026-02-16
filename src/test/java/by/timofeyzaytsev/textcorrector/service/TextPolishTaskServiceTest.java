package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.dto.response.FindTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.entity.enums.Language;
import by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.exception.NotFoundException;
import by.timofeyzaytsev.textcorrector.mapper.TextPolishTaskMapper;
import by.timofeyzaytsev.textcorrector.repository.TextPolishTaskRepository;
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
public class TextPolishTaskServiceTest {

    @Mock
    private TextPolishTaskRepository textPolishTaskRepository;

    @Mock
    private TextPolishTaskMapper textPolishTaskMapper;

    @InjectMocks
    private TextPolishTaskService textPolishTaskService;

    @Test
    void createTextCorrection_WhenRequestIsValid_ShouldReturnCreateCorrectionTaskResponse() {
        // Arrange
        CreateTextPolishTaskRequest request =
                new CreateTextPolishTaskRequest("Hello World", Language.EN);

        TextPolishTask task = TextPolishTask.builder().text("Hello World")
                .language(Language.EN)
                .status(TextPolishTaskStatus.NEW_TASK)
                .id(UUID.randomUUID())
                .build();

        when(textPolishTaskMapper.toCorrectionTask(request)).thenReturn(task);
        when(textPolishTaskRepository.save(task)).thenReturn(task);

        UUID expectedTaskId = task.getId();

        // Act
        CreateTextPolishTaskResponse actual = textPolishTaskService.createCorrectionTask(request);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.taskId()).isEqualTo(expectedTaskId);

        verify(textPolishTaskMapper, times(1)).toCorrectionTask(request);
        verify(textPolishTaskRepository, times(1)).save(task);
        verifyNoMoreInteractions(textPolishTaskMapper, textPolishTaskRepository);
    }

    @Test
    void findCorrectionTask_WhenTaskFinished_ShouldReturnResponseWithTask() {
        // Arrange
        UUID testId = UUID.randomUUID();
        TextPolishTask finishedTask = new TextPolishTask();
        finishedTask.setId(testId);
        finishedTask.setStatus(TextPolishTaskStatus.FINISHED);
        finishedTask.setText("исправленный текст");

        FindTextPolishTaskResponse expectedResponse = new FindTextPolishTaskResponse(
                "исправленный текст",
                TextPolishTaskStatus.FINISHED,
                null
        );

        when(textPolishTaskRepository.findCorrectionTaskById(testId))
                .thenReturn(Optional.of(finishedTask));
        when(textPolishTaskMapper.toFindCorrectionTaskResponse(finishedTask))
                .thenReturn(expectedResponse);

        // Act
        FindTextPolishTaskResponse actualResponse = textPolishTaskService.findCorrectionTask(testId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        assertEquals("исправленный текст", actualResponse.text());
        assertEquals(TextPolishTaskStatus.FINISHED, actualResponse.status());
        assertNull(actualResponse.errorDescription());

        verify(textPolishTaskRepository).findCorrectionTaskById(testId);
        verify(textPolishTaskMapper).toFindCorrectionTaskResponse(finishedTask);
        verifyNoMoreInteractions(textPolishTaskRepository, textPolishTaskMapper);
    }

    @Test
    void findCorrectionTask_WhenTaskNotFinished_ShouldReturnResponseWithNullTask() {
        // Arrange
        UUID testId = UUID.randomUUID();
        TextPolishTask inProgressTask = new TextPolishTask();
        inProgressTask.setId(testId);
        inProgressTask.setStatus(TextPolishTaskStatus.PROCESSING);
        inProgressTask.setText("текст который не должен быть в ответе");

        FindTextPolishTaskResponse expectedResponse = new FindTextPolishTaskResponse(
                null,
                TextPolishTaskStatus.PROCESSING,
                null
        );

        when(textPolishTaskRepository.findCorrectionTaskById(testId))
                .thenReturn(Optional.of(inProgressTask));
        when(textPolishTaskMapper.toFindCorrectionTaskResponse(argThat(task ->
                task.getStatus() == TextPolishTaskStatus.PROCESSING &&
                        task.getText() == null
        ))).thenReturn(expectedResponse);

        // Act
        FindTextPolishTaskResponse actualResponse = textPolishTaskService.findCorrectionTask(testId);

        // Assert
        assertNotNull(actualResponse);
        assertNull(actualResponse.text());
        assertEquals(TextPolishTaskStatus.PROCESSING, actualResponse.status());

        verify(textPolishTaskRepository).findCorrectionTaskById(testId);
        verify(textPolishTaskMapper).toFindCorrectionTaskResponse(argThat(task -> {
            assertNull(task.getText());
            return true;
        }));
    }

    @Test
    void findCorrectionTask_WhenTaskNotFound_ShouldThrowNotFoundException() {
        // Arrange
        UUID testId = UUID.randomUUID();
        when(textPolishTaskRepository.findCorrectionTaskById(testId))
                .thenReturn(Optional.empty());

        // Act and Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> textPolishTaskService.findCorrectionTask(testId)
        );

        assertEquals("Task with id: " + testId + " not found", exception.getMessage());

        verify(textPolishTaskRepository).findCorrectionTaskById(testId);
        verify(textPolishTaskMapper, never()).toFindCorrectionTaskResponse(any());
    }
}
