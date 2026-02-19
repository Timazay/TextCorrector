package by.timofeyzaytsev.textpolish.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CallYandexSpellerClient {

    private final YandexSpellerApiClient yandexSpellerApiClient;
    private final YandexSpellerCheckMapper yandexSpellerCheckMapper;
    private final static int IGNORE_DIGITS = 2;
    private final static int IGNORE_URLS = 4;

    public List<List<YandexSpellerCheckResponse>> execute(String text, String language, boolean ignoreDigits, boolean ignoreUrls) {
        YandexSpellerCheckRequest request = yandexSpellerCheckMapper
                .toYandexSpellerCheckRequest(language, List.of(text), calculateOptions(ignoreDigits, ignoreUrls));

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
