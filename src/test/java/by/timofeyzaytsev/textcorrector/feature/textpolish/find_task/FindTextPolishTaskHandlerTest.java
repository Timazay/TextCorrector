package by.timofeyzaytsev.textcorrector.feature.textpolish.find_task;


import by.timofeyzaytsev.textcorrector.common.exception.NotFoundException;
import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskHandler;
import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskMapper;
import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.infrastructure.repository.TextPolishTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindTextPolishTaskHandlerTest {

    @Mock
    private FindTextPolishTaskMapper findTextPolishTaskMapper;
    @Mock
    private TextPolishTaskRepository textPolishTaskRepository;
    @InjectMocks
    private FindTextPolishTaskHandler handler;

    @Test
    void findTextPolishTask_WhenTaskFinished_ShouldReturnResponseWithTask() {
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
        when(findTextPolishTaskMapper.toFindCorrectionTaskResponse(finishedTask))
                .thenReturn(expectedResponse);

        // Act
        FindTextPolishTaskResponse actualResponse = handler.execute(testId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        assertEquals("исправленный текст", actualResponse.text());
        assertEquals(TextPolishTaskStatus.FINISHED, actualResponse.status());
        assertNull(actualResponse.errorDescription());

        verify(textPolishTaskRepository).findCorrectionTaskById(testId);
        verify(findTextPolishTaskMapper).toFindCorrectionTaskResponse(finishedTask);
        verifyNoMoreInteractions(textPolishTaskRepository, findTextPolishTaskMapper);
    }

    @Test
    void findTextPolishTask_WhenTaskNotFinished_ShouldReturnResponseWithNullTask() {
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
        when(findTextPolishTaskMapper.toFindCorrectionTaskResponse(argThat(task ->
                task.getStatus() == TextPolishTaskStatus.PROCESSING &&
                        task.getText() == null
        ))).thenReturn(expectedResponse);

        // Act
        FindTextPolishTaskResponse actualResponse = handler.execute(testId);

        // Assert
        assertNotNull(actualResponse);
        assertNull(actualResponse.text());
        assertEquals(TextPolishTaskStatus.PROCESSING, actualResponse.status());

        verify(textPolishTaskRepository).findCorrectionTaskById(testId);
        verify(findTextPolishTaskMapper).toFindCorrectionTaskResponse(argThat(task -> {
            assertNull(task.getText());
            return true;
        }));
    }

    @Test
    void findTextPolishTask_WhenTaskNotFound_ShouldThrowNotFoundException() {
        // Arrange
        UUID testId = UUID.randomUUID();
        when(textPolishTaskRepository.findCorrectionTaskById(testId))
                .thenReturn(Optional.empty());

        // Act and Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> handler.execute(testId)
        );

        assertEquals("Task with id: " + testId + " not found", exception.getMessage());

        verify(textPolishTaskRepository).findCorrectionTaskById(testId);
        verify(findTextPolishTaskMapper, never()).toFindCorrectionTaskResponse(any());
    }
}
