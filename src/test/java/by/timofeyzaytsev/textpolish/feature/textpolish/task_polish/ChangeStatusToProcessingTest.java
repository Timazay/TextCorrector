package by.timofeyzaytsev.textpolish.feature.textpolish.task_polish;

import by.timofeyzaytsev.textpolish.features.textpolish.task_polish.ChangeStatusToProcessing;
import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textpolish.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textpolish.infrastructure.repository.TextPolishTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChangeStatusToProcessingTest {

    @Mock
    private TextPolishTaskRepository textPolishTaskRepository;

    @InjectMocks
    private ChangeStatusToProcessing changeStatusToProcessing;

    TextPolishTask newTaskOne;
    TextPolishTask newTaskTwo;
    TextPolishTask processingTask;

    @BeforeEach
    void setUp() {
        newTaskOne = TextPolishTask.builder()
                .id(UUID.randomUUID())
                .text("some text")
                .status(TextPolishTaskStatus.NEW_TASK)
                .build();
        newTaskTwo = TextPolishTask.builder()
                .id(UUID.randomUUID())
                .text("some text")
                .status(TextPolishTaskStatus.NEW_TASK)
                .build();
        processingTask = TextPolishTask.builder()
                .id(UUID.randomUUID())
                .text("some text")
                .status(TextPolishTaskStatus.PROCESSING)
                .count(2)
                .expiration(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    @Test
    void change_WhenFindListOfNewAndProcessingTasks_ShouldReturnProcessingTasks() {
        // Assert
        List<TextPolishTask> mockTasks = Arrays.asList(newTaskOne, newTaskTwo, processingTask);

        when(textPolishTaskRepository.findNewAndProcessingTasks(PageRequest.of(0, 10)))
                .thenReturn(mockTasks);

        // Act
        List<TextPolishTask> result = changeStatusToProcessing.change();

        // Arrange
        verify(textPolishTaskRepository, times(1))
                .findNewAndProcessingTasks(PageRequest.of(0, 10));

        assertThat(newTaskOne.getStatus()).isEqualTo(TextPolishTaskStatus.PROCESSING);
        assertThat(newTaskOne.getCount()).isEqualTo(1);
        assertThat(newTaskOne.getExpiration()).isNotNull();

        assertThat(newTaskTwo.getStatus()).isEqualTo(TextPolishTaskStatus.PROCESSING);
        assertThat(newTaskTwo.getCount()).isEqualTo(1);
        assertThat(newTaskTwo.getExpiration()).isNotNull();

        assertThat(processingTask.getStatus()).isEqualTo(TextPolishTaskStatus.PROCESSING);
        assertThat(processingTask.getCount()).isEqualTo(3);

        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(newTaskOne, newTaskTwo, processingTask);
    }
}
