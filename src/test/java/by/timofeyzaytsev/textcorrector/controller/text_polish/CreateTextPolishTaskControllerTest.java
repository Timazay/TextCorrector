package by.timofeyzaytsev.textcorrector.controller.text_polish;

import by.timofeyzaytsev.textcorrector.controller.textpolish.CreateTextPolishTaskController;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskHandler;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.Language;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateTextPolishTaskControllerTest.class)
@Import(CreateTextPolishTaskController.class)
public class CreateTextPolishTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTextPolishTaskHandler handler;

    @Test
    void createTask_ShouldReturnTaskId_WhenRequestIsValid() throws Exception {
        // Arrange
        CreateTextPolishTaskRequest request = new CreateTextPolishTaskRequest(
                "Hello World",
                Language.EN
        );

        UUID expectedTaskId = UUID.randomUUID();
        CreateTextPolishTaskResponse response = new CreateTextPolishTaskResponse(expectedTaskId);

        when(handler.execute(any(CreateTextPolishTaskRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/text-polish-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(expectedTaskId.toString()));
    }

    @Test
    void createTask_ShouldReturnBadRequest_WhenIsEmptyTask() throws Exception {
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
    void createTask_ShouldReturnBadRequest_WhenHasLessThenThreeLettersTask() throws Exception {
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
    void createTask_ShouldReturnBadRequest_WhenHasNoLetterTask() throws Exception {
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
}
