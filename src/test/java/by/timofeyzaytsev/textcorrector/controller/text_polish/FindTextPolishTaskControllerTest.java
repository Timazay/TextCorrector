package by.timofeyzaytsev.textcorrector.controller.text_polish;

import by.timofeyzaytsev.textcorrector.controller.textpolish.FindTextPolishTaskController;
import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskHandler;
import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FindTextPolishTaskController.class)
@Import(FindTextPolishTaskController.class)
public class FindTextPolishTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FindTextPolishTaskHandler handler;

    @Test
    void findTextPolishTask_WhenTaskFinished_ShouldReturnResponseWithTask() throws Exception {
        // Arrange
        UUID testId = UUID.randomUUID();
        FindTextPolishTaskResponse expectedResponse = new FindTextPolishTaskResponse(
                "исправленный текст",
                TextPolishTaskStatus.FINISHED,
                null
        );

        when(handler.execute(testId))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/text-polish-tasks/" + testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("исправленный текст"))
                .andExpect(jsonPath("$.status").value("FINISHED"))
                .andExpect(jsonPath("$.errorDescription").doesNotExist());

        verify(handler, times(1)).execute(testId);
    }

    @Test
    void findTextPolishTask_WhenTaskIsFailed_ShouldReturnResponseWithNullTask() throws Exception {
        // Arrange
        UUID testId = UUID.randomUUID();
        FindTextPolishTaskResponse expectedResponse = new FindTextPolishTaskResponse(
                null,
                TextPolishTaskStatus.FAILED,
                "Something went wrong"
        );

        when(handler.execute(testId))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/text-polish-tasks/" + testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").doesNotExist())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.errorDescription").value("Something went wrong"));

        verify(handler, times(1)).execute(testId);
    }

    @Test
    void findTextPolishTask_WhenTaskIsProcessing_ShouldReturnResponseWithStatus() throws Exception {
        // Arrange
        UUID testId = UUID.randomUUID();
        FindTextPolishTaskResponse expectedResponse = new FindTextPolishTaskResponse(
                null,
                TextPolishTaskStatus.PROCESSING,
                null
        );

        when(handler.execute(testId))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/text-polish-tasks/" + testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").doesNotExist())
                .andExpect(jsonPath("$.status").value("PROCESSING"))
                .andExpect(jsonPath("$.errorDescription").doesNotExist());

        verify(handler, times(1)).execute(testId);
    }
}
