package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.dto.request.YandexSpellCheckRequest;
import by.timofeyzaytsev.textcorrector.dto.response.YandexSpellCheckResponse;
import by.timofeyzaytsev.textcorrector.client.ExternalYandexSpellerApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static by.timofeyzaytsev.textcorrector.utils.StringUtils.splitIntoBlocks;

@Service
@RequiredArgsConstructor
public class YandexSpellService {

    private final ExternalYandexSpellerApiClient externalYandexSpellerApiClient;
    private static final int MAX_TEXT_LENGTH = 1000;

    /**
     * Метод отправляет запрос к методу post checkText частями (если строка превышает 1000 символов)
     * @return возвращает строку с исправленным текстом.
     */
    public String checkAndCorrectText(String task, YandexSpellCheckRequest request) {
        List<String> list = splitIntoBlocks(task, MAX_TEXT_LENGTH);
        return list.stream()
                .map(textBlock -> {
                    List<List<YandexSpellCheckResponse>> responses =
                            externalYandexSpellerApiClient.checkText(textBlock, request.lang(), request.options());

                    List<YandexSpellCheckResponse> flatResponses = responses.stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

                    return processResponse(textBlock, flatResponses);
                })
                .collect(Collectors.joining());
    }

    /**
     * Метод исправления текста на основе ответа яндекс апи
     *
     * @param task        исходный текст
     * @param corrections список исправлений от API
     * @return исправленный текст
     */
    private String processResponse(String task, List<YandexSpellCheckResponse> corrections) {
        StringBuilder correctedText = new StringBuilder(task);

        corrections.stream()
                .filter(c -> c.s() != null && !c.s().isEmpty())
                .sorted(Comparator.comparing(YandexSpellCheckResponse::pos).reversed())
                .forEach(c -> correctedText.replace(
                        c.pos(),
                        c.pos() + c.len(),
                        c.s().getFirst()
                ));

        return correctedText.toString();
    }
}
