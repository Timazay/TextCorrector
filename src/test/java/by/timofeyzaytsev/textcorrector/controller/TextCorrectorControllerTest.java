package by.timofeyzaytsev.textcorrector.controller;

import by.timofeyzaytsev.textcorrector.dto.request.CreateCorrectionTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.dto.response.FindCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskLanguage;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus;
import by.timofeyzaytsev.textcorrector.service.TextCorrectorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TextCorrectorController.class)
public class TextCorrectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TextCorrectorService textCorrectorService;

    @Test
    void createCorrectionTask_ShouldReturnTaskId_WhenRequestIsValid() throws Exception {
        // Arrange
        CreateCorrectionTaskRequest request = new CreateCorrectionTaskRequest(
                "Hello World",
                CorrectionTaskLanguage.EN
        );

        UUID expectedTaskId = UUID.randomUUID();
        CreateCorrectionTaskResponse response = new CreateCorrectionTaskResponse(expectedTaskId);

        when(textCorrectorService.createCorrectionTask(any(CreateCorrectionTaskRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/text/corrections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request) ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(expectedTaskId.toString()));
    }

    @Test
    void createTextCorrectionTask_ShouldReturnBadRequest_WhenIsEmptyTask() throws Exception {
        // Arrange
        CreateCorrectionTaskRequest request = new CreateCorrectionTaskRequest(
                "",
                CorrectionTaskLanguage.EN
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/text/corrections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCorrectionTask_ShouldReturnBadRequest_WhenHasLessThenThreeLettersTask() throws Exception {
        // Arrange
        CreateCorrectionTaskRequest request = new CreateCorrectionTaskRequest(
                "as",
                CorrectionTaskLanguage.EN
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/text/corrections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCorrectionTask_ShouldReturnBadRequest_WhenHasNoLetterTask() throws Exception {
        // Arrange
        CreateCorrectionTaskRequest request = new CreateCorrectionTaskRequest(
                "123%&",
                CorrectionTaskLanguage.EN
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/text/corrections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findCorrectionTask_WhenTaskFinished_ShouldReturnResponseWithTask() throws Exception {
        // Arrange
        UUID testId = UUID.randomUUID();
        FindCorrectionTaskResponse expectedResponse = new FindCorrectionTaskResponse(
                "исправленный текст",
                CorrectionTaskStatus.FINISHED
        );

        when(textCorrectorService.findCorrectionTask(testId))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/text/corrections")
                        .param("id", testId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("исправленный текст"))
                .andExpect(jsonPath("$.status").value("FINISHED"));

        verify(textCorrectorService, times(1)).findCorrectionTask(testId);
    }

    @Test
    void findCorrectionTask_WhenTaskIsFailed_ShouldReturnResponseWithNullTask() throws Exception {
        // Arrange
        UUID testId = UUID.randomUUID();
        FindCorrectionTaskResponse expectedResponse = new FindCorrectionTaskResponse(
                null,
                CorrectionTaskStatus.FAILED
        );

        when(textCorrectorService.findCorrectionTask(testId))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/text/corrections")
                        .param("id", testId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").doesNotExist())
                .andExpect(jsonPath("$.status").value("FAILED"));

        verify(textCorrectorService, times(1)).findCorrectionTask(testId);
    }
}
