package by.timofeyzaytsev.textpolish.infrastructure.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CallYandexSpellerClientTest {

    @Mock
    private YandexSpellerApiClient yandexSpellerApiClient;

    @Mock
    private YandexSpellerCheckMapper yandexSpellerCheckMapper;

    @InjectMocks
    private CallYandexSpellerClient callYandexSpellerClient;

    @Test
    void execute_WhenNoFlagsSet_shouldExecuteWithDefaultOptions() {
        // Arrange
        String text = "Hello world";
        String language = "ru";
        boolean ignoreDigits = false;
        boolean ignoreUrls = false;

        YandexSpellerCheckRequest request = new YandexSpellerCheckRequest(List.of(text), language, 0);

        when(yandexSpellerCheckMapper.toYandexSpellerCheckRequest(
                eq(language),
                eq(List.of(text)),
                anyInt()
        )).thenReturn(request);

        List<List<YandexSpellerCheckResponse>> expectedResponse = List.of(List.of());
        when(yandexSpellerApiClient.checkText(
                request.text(),
                request.lang(),
                request.options()
        )).thenReturn(expectedResponse);

        // Act
        List<List<YandexSpellerCheckResponse>> result =
                callYandexSpellerClient.execute(text, language, ignoreDigits, ignoreUrls);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(yandexSpellerCheckMapper).toYandexSpellerCheckRequest(
                eq(language),
                eq(List.of(text)),
                eq(0)
        );
    }

    @Test
    void execute_WhenIgnoreDigitsIsTrue_ShouldCalculateOptionsWithIgnoreDigits() {
        // Arrange
        String text = "Hello world 123";
        String language = "en";
        boolean ignoreDigits = true;
        boolean ignoreUrls = false;
        int expectedOptions = 2;

        YandexSpellerCheckRequest request =
                new YandexSpellerCheckRequest(List.of(text), language, expectedOptions);

        when(yandexSpellerCheckMapper.toYandexSpellerCheckRequest(
                eq(language),
                eq(List.of(text)),
                anyInt()
        )).thenReturn(request);

        List<List<YandexSpellerCheckResponse>> expectedResponse = List.of(List.of());
        when(yandexSpellerApiClient.checkText(
                request.text(),
                request.lang(),
                request.options()
        )).thenReturn(expectedResponse);

        // Act
        List<List<YandexSpellerCheckResponse>> result =
                callYandexSpellerClient.execute(text, language, ignoreDigits, ignoreUrls);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(yandexSpellerCheckMapper).toYandexSpellerCheckRequest(
                eq(language),
                eq(List.of(text)),
                eq(expectedOptions)
        );
    }

    @Test
    void execute_WhenIgnoreUrlIsTrue_ShouldCalculateOptionsWithIgnoreUrlsOption() {
        // Arrange
        String text = "Check this url: https://example.com";
        String language = "en";
        boolean ignoreDigits = false;
        boolean ignoreUrls = true;
        int expectedOptions = 4;

        YandexSpellerCheckRequest request =
                new YandexSpellerCheckRequest(List.of(text), language, expectedOptions);

        when(yandexSpellerCheckMapper.toYandexSpellerCheckRequest(
                eq(language),
                eq(List.of(text)),
                anyInt()
        )).thenReturn(request);

        List<List<YandexSpellerCheckResponse>> expectedResponse = List.of(List.of());
        when(yandexSpellerApiClient.checkText(
                request.text(),
                request.lang(),
                request.options()
        )).thenReturn(expectedResponse);

        // Act
        List<List<YandexSpellerCheckResponse>> result =
                callYandexSpellerClient.execute(text, language, ignoreDigits, ignoreUrls);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(yandexSpellerCheckMapper).toYandexSpellerCheckRequest(
                eq(language),
                eq(List.of(text)),
                eq(expectedOptions)
        );
    }

    @Test
    void execute_WhenBothIgnoreParametersIsTrue_ShouldCalculateOptionsWithBothOptionsEnabled() {
        // Arrange
        String text = "Text with 123 and https://example.com";
        String language = "en";
        boolean ignoreDigits = true;
        boolean ignoreUrls = true;
        int expectedOptions = 6;

        YandexSpellerCheckRequest request =
                new YandexSpellerCheckRequest(List.of(text), language, expectedOptions);

        when(yandexSpellerCheckMapper.toYandexSpellerCheckRequest(
                eq(language),
                eq(List.of(text)),
                anyInt()
        )).thenReturn(request);

        List<List<YandexSpellerCheckResponse>> expectedResponse = List.of(List.of());
        when(yandexSpellerApiClient.checkText(
                request.text(),
                request.lang(),
                request.options()
        )).thenReturn(expectedResponse);

        // Act
        List<List<YandexSpellerCheckResponse>> result =
                callYandexSpellerClient.execute(text, language, ignoreDigits, ignoreUrls);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(yandexSpellerCheckMapper).toYandexSpellerCheckRequest(
                eq(language),
                eq(List.of(text)),
                eq(expectedOptions)
        );
    }
}
