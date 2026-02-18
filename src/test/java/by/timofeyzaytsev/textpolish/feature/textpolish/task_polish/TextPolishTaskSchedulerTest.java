package by.timofeyzaytsev.textpolish.feature.textpolish.task_polish;

import by.timofeyzaytsev.textpolish.features.textpolish.task_polish.ChangeStatusToProcessing;
import by.timofeyzaytsev.textpolish.features.textpolish.task_polish.ProcessTextPolishTaskHandler;
import by.timofeyzaytsev.textpolish.features.textpolish.task_polish.TextPolishTaskMapper;
import by.timofeyzaytsev.textpolish.features.textpolish.task_polish.TextPolishTaskScheduler;
import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textpolish.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textpolish.infrastructure.repository.TextPolishTaskRepository;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TextPolishTaskSchedulerTest {

    @Mock
    private ProcessTextPolishTaskHandler handler;

    @Mock
    private TextPolishTaskRepository textPolishTaskRepository;

    @Mock
    private TextPolishTaskMapper textPolishTaskMapper;

    @Mock
    private ChangeStatusToProcessing changeStatusToProcessing;

    @InjectMocks
    private TextPolishTaskScheduler scheduler;

    @Captor
    private ArgumentCaptor<TextPolishTask> taskCaptor;

    private TextPolishTask processingTaskOne;
    private TextPolishTask processingTaskTwo;

    @BeforeEach
    void setUp() {
        processingTaskOne = new TextPolishTask();
        processingTaskOne.setId(UUID.randomUUID());
        processingTaskOne.setStatus(TextPolishTaskStatus.PROCESSING);
        processingTaskOne.setCount(1);
        processingTaskOne.setText("Sample text");

        processingTaskTwo = new TextPolishTask();
        processingTaskTwo.setId(UUID.randomUUID());
        processingTaskTwo.setStatus(TextPolishTaskStatus.PROCESSING);
        processingTaskTwo.setCount(1);
        processingTaskTwo.setText("Another text");
    }

    @Test
    void processTexts_WhenTasksAreFound_ShouldProcessTasksSuccessfully() {
        // Arrange
        List<TextPolishTask> tasks = Arrays.asList(processingTaskOne, processingTaskTwo);
        when(changeStatusToProcessing.change()).thenReturn(tasks);
        String processedText = "Processed text";
        when(handler.execute(any(TextPolishTask.class), anyBoolean(), anyBoolean()))
                .thenReturn(processedText);

        doAnswer(invocation -> {
            TextPolishTask task = invocation.getArgument(0);
            task.setStatus(TextPolishTaskStatus.FINISHED);
            task.setText(processedText);
            return null;
        }).when(textPolishTaskMapper).toFinishTask(processingTaskOne, processedText);

        doAnswer(invocation -> {
            TextPolishTask task = invocation.getArgument(0);
            task.setStatus(TextPolishTaskStatus.FINISHED);
            task.setText(processedText);
            return null;
        }).when(textPolishTaskMapper).toFinishTask(processingTaskTwo, processedText);


        // Act
        scheduler.processTexts();

        //Assert
        verify(changeStatusToProcessing, times(1)).change();
        verify(handler, times(2)).execute(any(TextPolishTask.class), anyBoolean(), anyBoolean());
        verify(textPolishTaskMapper, times(2)).toFinishTask(any(TextPolishTask.class), anyString());
        verify(textPolishTaskRepository, times(2)).save(any(TextPolishTask.class));

        verify(textPolishTaskRepository).save(processingTaskOne);
        verify(textPolishTaskRepository).save(processingTaskTwo);
        assertThat(processingTaskOne.getStatus()).isEqualTo(TextPolishTaskStatus.FINISHED);
        assertThat(processingTaskTwo.getStatus()).isEqualTo(TextPolishTaskStatus.FINISHED);
        assertThat(processingTaskOne.getText()).isEqualTo(processedText);
        assertThat(processingTaskTwo.getText()).isEqualTo(processedText);
    }

    @Test
    void processTexts_WhenCountMoreThenFour_ShouldCatchProcessingLimitExceededExceptionAndFailTask() {
        // Arrange
        processingTaskOne.setCount(4);
        List<TextPolishTask> tasks = List.of(processingTaskOne);
        when(changeStatusToProcessing.change()).thenReturn(tasks);
        when(handler.execute(any(TextPolishTask.class), anyBoolean(), anyBoolean()))
                .thenThrow(FeignException.errorStatus(
                        "methodKey",
                        Response.builder()
                                .status(408 )
                                .reason("Timeout")
                                .request(Request.create(
                                        Request.HttpMethod.POST,
                                        "http://yandex.com",
                                        Collections.emptyMap(),
                                        new byte[0],
                                        StandardCharsets.UTF_8,
                                        null
                                ))
                                .build()
                ));

        // Act
        scheduler.processTexts();

        // Assert
        verify(changeStatusToProcessing, times(1)).change();
        verify(handler, times(1)).execute(any(), anyBoolean(), anyBoolean());
        verify(textPolishTaskMapper, never()).toFinishTask(any(), any());

        verify(textPolishTaskRepository).save(taskCaptor.capture());
        TextPolishTask savedTask = taskCaptor.getValue();

        assertThat(savedTask.getId()).isEqualTo(processingTaskOne.getId());
        assertThat(savedTask.getStatus()).isEqualTo(TextPolishTaskStatus.FAILED);
        assertThat(savedTask.getErrorDescription()).contains("Timeout");
    }

    @Test
    void processTexts_WhenTasksIdEmpty_ShouldReturnNothing() {
        // Arrange
        when(changeStatusToProcessing.change()).thenReturn(Collections.emptyList());

        // Act
        scheduler.processTexts();

        // Assert
        verify(changeStatusToProcessing, times(1)).change();
        verify(handler, never()).execute(any(), anyBoolean(), anyBoolean());
        verify(textPolishTaskMapper, never()).toFinishTask(any(), any());
        verify(textPolishTaskRepository, never()).save(any());
    }
}
