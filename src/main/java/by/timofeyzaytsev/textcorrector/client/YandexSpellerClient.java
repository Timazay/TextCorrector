package by.timofeyzaytsev.textcorrector.client;

import by.timofeyzaytsev.textcorrector.dto.request.YandexSpellerCheckRequest;
import by.timofeyzaytsev.textcorrector.dto.response.YandexSpellerCheckResponse;
import by.timofeyzaytsev.textcorrector.mapper.TextPolishTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class YandexSpellerClient {

    private final YandexSpellerApiClient yandexSpellerApiClient;
    private final TextPolishTaskMapper textPolishTaskMapper;
    private final static int IGNORE_DIGITS = 2;
    private final static int IGNORE_URLS = 4;

    @Retryable
    public List<List<YandexSpellerCheckResponse>> checkText(String text, String language, boolean ignoreDigits, boolean ignoreUrls) {
        YandexSpellerCheckRequest request = textPolishTaskMapper.toYandexSpellerCheckRequest(language,
                List.of(text), calculateOptions(ignoreDigits, ignoreUrls));

        return yandexSpellerApiClient.checkText(request.text(), request.lang(), request.options());
    }

    private int calculateOptions(boolean ignoreDigits, boolean ignoreUrls) {
        int options = 0;

        if (ignoreDigits)
            options += IGNORE_DIGITS;

        if (ignoreUrls)
            options += IGNORE_URLS;

        return options;
    }
}
