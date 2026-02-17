package by.timofeyzaytsev.textcorrector.feature.textpolish.create_task;

import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskHandler;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskMapper;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.Language;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.infrastructure.repository.TextPolishTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTextPolishTaskHandlerTest {

    @Mock
    private CreateTextPolishTaskMapper createTextPolishTaskMapper;
    @Mock
    private TextPolishTaskRepository textPolishTaskRepository;
    @InjectMocks
    private CreateTextPolishTaskHandler createTextPolishTaskHandler;

    @Test
    void createTextPolishTask_WhenRequestIsValid_ShouldReturnCreateTextPolishTaskResponse() {
        // Arrange
        CreateTextPolishTaskRequest request =
                new CreateTextPolishTaskRequest("Hello World", Language.EN);

        TextPolishTask task = TextPolishTask.builder().text("Hello World")
                .language(Language.EN)
                .status(TextPolishTaskStatus.NEW_TASK)
                .id(UUID.randomUUID())
                .build();

        when(createTextPolishTaskMapper.toCorrectionTask(request)).thenReturn(task);
        when(textPolishTaskRepository.save(task)).thenReturn(task);

        UUID expectedTaskId = task.getId();

        // Act
        CreateTextPolishTaskResponse actual = createTextPolishTaskHandler.execute(request);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.taskId()).isEqualTo(expectedTaskId);

        verify(createTextPolishTaskMapper, times(1)).toCorrectionTask(request);
        verify(textPolishTaskRepository, times(1)).save(task);
        verifyNoMoreInteractions(createTextPolishTaskMapper, textPolishTaskRepository);
    }
}
