package by.timofeyzaytsev.textcorrector.controller;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextCorrectionRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateTextCorrectionResponse;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskLanguage;
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
import static org.mockito.Mockito.when;
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
    void createTextCorrection_ShouldReturnTaskId_WhenRequestIsValid() throws Exception {
        // Arrange
        CreateTextCorrectionRequest request = new CreateTextCorrectionRequest(
                "Hello World",
                CorrectionTaskLanguage.EN
        );

        UUID expectedTaskId = UUID.randomUUID();
        CreateTextCorrectionResponse response = new CreateTextCorrectionResponse(expectedTaskId);

        when(textCorrectorService.createTextCorrection(any(CreateTextCorrectionRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/text/corrections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request) ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(expectedTaskId.toString()));
    }

    @Test
    void createTextCorrection_ShouldReturnBadRequest_WhenTextIsEmpty() throws Exception {
        // Arrange
        CreateTextCorrectionRequest request = new CreateTextCorrectionRequest(
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
    void createTextCorrection_ShouldReturnBadRequest_WhenTextHasLessThenThreeLetters() throws Exception {
        // Arrange
        CreateTextCorrectionRequest request = new CreateTextCorrectionRequest(
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
    void createTextCorrection_ShouldReturnBadRequest_WhenTextHasNoLetter() throws Exception {
        // Arrange
        CreateTextCorrectionRequest request = new CreateTextCorrectionRequest(
                "123%&",
                CorrectionTaskLanguage.EN
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/text/corrections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
