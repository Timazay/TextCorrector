package by.timofeyzaytsev.textcorrector.controller;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.dto.response.FindTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.entity.enums.Language;
import by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.service.TextPolishTaskService;
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

@WebMvcTest(TextPolishTaskController.class)
public class TextPolishTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TextPolishTaskService textPolishTaskService;

    @Test
    void createCorrectionTask_ShouldReturnTaskId_WhenRequestIsValid() throws Exception {
        // Arrange
        CreateTextPolishTaskRequest request = new CreateTextPolishTaskRequest(
                "Hello World",
                Language.EN
        );

        UUID expectedTaskId = UUID.randomUUID();
        CreateTextPolishTaskResponse response = new CreateTextPolishTaskResponse(expectedTaskId);

        when(textPolishTaskService.createCorrectionTask(any(CreateTextPolishTaskRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/text-polish-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(expectedTaskId.toString()));
    }

    @Test
    void createTextCorrectionTask_ShouldReturnBadRequest_WhenIsEmptyTask() throws Exception {
        // Arrange
        CreateTextPolishTaskRequest request = new CreateTextPolishTaskRequest(
                "",
                Language.EN
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/text-polish-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCorrectionTask_ShouldReturnBadRequest_WhenHasLessThenThreeLettersTask() throws Exception {
        // Arrange
        CreateTextPolishTaskRequest request = new CreateTextPolishTaskRequest(
                "as",
                Language.EN
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/text-polish-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCorrectionTask_ShouldReturnBadRequest_WhenHasNoLetterTask() throws Exception {
        // Arrange
        CreateTextPolishTaskRequest request = new CreateTextPolishTaskRequest(
                "123%&",
                Language.EN
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/text-polish-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findCorrectionTask_WhenTaskFinished_ShouldReturnResponseWithTask() throws Exception {
        // Arrange
        UUID testId = UUID.randomUUID();
        FindTextPolishTaskResponse expectedResponse = new FindTextPolishTaskResponse(
                "исправленный текст",
                TextPolishTaskStatus.FINISHED,
                null
        );

        when(textPolishTaskService.findCorrectionTask(testId))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/text-polish-tasks/" + testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("исправленный текст"))
                .andExpect(jsonPath("$.status").value("FINISHED"))
                .andExpect(jsonPath("$.errorDescription").doesNotExist());

        verify(textPolishTaskService, times(1)).findCorrectionTask(testId);
    }

    @Test
    void findCorrectionTask_WhenTaskIsFailed_ShouldReturnResponseWithNullTask() throws Exception {
        // Arrange
        UUID testId = UUID.randomUUID();
        FindTextPolishTaskResponse expectedResponse = new FindTextPolishTaskResponse(
                null,
                TextPolishTaskStatus.FAILED,
                "Something went wrong"
        );

        when(textPolishTaskService.findCorrectionTask(testId))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/text-polish-tasks/" + testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").doesNotExist())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.errorDescription").value("Something went wrong"));

        verify(textPolishTaskService, times(1)).findCorrectionTask(testId);
    }
}
