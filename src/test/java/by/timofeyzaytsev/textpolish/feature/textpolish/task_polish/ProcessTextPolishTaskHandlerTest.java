package by.timofeyzaytsev.textpolish.feature.textpolish.task_polish;

import by.timofeyzaytsev.textpolish.features.textpolish.task_polish.ProcessTextPolishTaskHandler;
import by.timofeyzaytsev.textpolish.infrastructure.client.CallYandexSpellerClient;
import by.timofeyzaytsev.textpolish.infrastructure.client.YandexSpellerCheckResponse;
import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textpolish.infrastructure.entity.enums.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessTextPolishTaskHandlerTest {

    @Mock
    private CallYandexSpellerClient callYandexSpellerClient;

    @InjectMocks
    private ProcessTextPolishTaskHandler handler;

    @Test
    void execute_WithShortText_ShouldProcessFullText() {
        // Arrange
        String text = "This is a short text with eror.";
        TextPolishTask task = TextPolishTask.builder()
                .text(text)
                .language(Language.EN)
                .build();

        List<List<YandexSpellerCheckResponse>> mockResponse = createMockResponse(26, 4, List.of("error"));

        when(callYandexSpellerClient.execute(eq(text), eq("EN"), eq(false), eq(false)))
                .thenReturn(mockResponse);

        // Act
        String result = handler.execute(task, false, false);

        // Assert
        assertThat(result).isEqualTo("This is a short text with error.");
    }

    @Test
    void execute_WithLongText_ShouldSplitIntoBlocks() {
        // Arrange
        String longText = "a".repeat(15000);
        TextPolishTask task = TextPolishTask.builder()
                .text(longText)
                .language(Language.RU)
                .build();

        String block1 = longText.substring(0, 10000);
        String block2 = longText.substring(10000);

        List<List<YandexSpellerCheckResponse>> mockResponse1 = createMockResponse(0, 0, List.of());
        List<List<YandexSpellerCheckResponse>> mockResponse2 = createMockResponse(0, 0, List.of());

        when(callYandexSpellerClient.execute(eq(block1), eq("RU"), eq(true), eq(true)))
                .thenReturn(mockResponse1);
        when(callYandexSpellerClient.execute(eq(block2), eq("RU"), eq(true), eq(true)))
                .thenReturn(mockResponse2);

        // Act
        String result = handler.execute(task, true, true);

        // Assert
        assertThat(result).isEqualTo(longText);
    }

    private List<List<YandexSpellerCheckResponse>> createMockResponse(int pos, int len, List<String> suggestions) {
        YandexSpellerCheckResponse response = YandexSpellerCheckResponse.builder()
                .pos(pos)
                .len(len)
                .s(suggestions)
                .build();
        return List.of(List.of(response));
    }
}
