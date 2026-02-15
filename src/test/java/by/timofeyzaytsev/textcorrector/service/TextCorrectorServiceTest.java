package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextCorrectionRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateTextCorrectionResponse;
import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskLanguage;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus;
import by.timofeyzaytsev.textcorrector.mapper.CorrectionTaskMapper;
import by.timofeyzaytsev.textcorrector.repository.CorrectionTaskRepository;
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
public class TextCorrectorServiceTest {

    @Mock
    private CorrectionTaskRepository correctionTaskRepository;

    @Mock
    private CorrectionTaskMapper correctionTaskMapper;

    @InjectMocks
    private TextCorrectorService textCorrectorService;

    @Test
    void createTextCorrection_WhenRequestIsValid_ShouldReturnCreateTextCorrectionResponse() {
        // Arrange
        CreateTextCorrectionRequest request =
                new CreateTextCorrectionRequest("Hello World", CorrectionTaskLanguage.EN);

        CorrectionTask task = CorrectionTask.builder().text("Hello World")
                .language(CorrectionTaskLanguage.EN)
                .status(CorrectionTaskStatus.NEW_TASK)
                .id(UUID.randomUUID())
                .build();

        when(correctionTaskMapper.toCorrectionTask(request)).thenReturn(task);
        when(correctionTaskRepository.save(task)).thenReturn(task);

        UUID expectedTaskId = task.getId();

        // Act
        CreateTextCorrectionResponse actual = textCorrectorService.createTextCorrection(request);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.taskId()).isEqualTo(expectedTaskId);

        verify(correctionTaskMapper, times(1)).toCorrectionTask(request);
        verify(correctionTaskRepository, times(1)).save(task);
        verifyNoMoreInteractions(correctionTaskMapper, correctionTaskRepository);
    }
}
