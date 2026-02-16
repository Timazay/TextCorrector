package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.client.YandexSpellerClient;
import by.timofeyzaytsev.textcorrector.dto.response.YandexSpellerCheckResponse;
import by.timofeyzaytsev.textcorrector.entity.TextPolishTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static by.timofeyzaytsev.textcorrector.utils.StringUtils.splitIntoBlocks;

@Service
@RequiredArgsConstructor
public class YandexSpellerService {

    private final YandexSpellerClient yandexSpellerClient;
    private static final int MAX_TEXT_LENGTH = 10000;

    /**
     * The method sends a request to the post checkText method in parts (if the string exceeds MAX_TEXT_LENGTH characters)
     * @return returns a string with the corrected text.
     */
    public String checkAndCorrectText(TextPolishTask task, boolean ignoreDigits, boolean ignoreUrls) {
        List<String> list = splitIntoBlocks(task.getText(), MAX_TEXT_LENGTH);
        return list.stream()
                .map(textBlock -> {
                    List<List<YandexSpellerCheckResponse>> responses =
                            yandexSpellerClient.checkText(task.getText(), task.getLanguage().name(),
                                    ignoreDigits, ignoreUrls);

                    List<YandexSpellerCheckResponse> flatResponses = responses.stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

                    return processResponse(textBlock, flatResponses);
                })
                .collect(Collectors.joining());
    }

    /**
     * Method for correcting text based on a Yandex API response
     *
     * @param task : original text
     * @param corrections : list of corrections from the API
     * @return : corrected text
     */
    private String processResponse(String task, List<YandexSpellerCheckResponse> corrections) {
        StringBuilder correctedText = new StringBuilder(task);

        corrections.stream()
                .filter(c -> c.s() != null && !c.s().isEmpty())
                .sorted(Comparator.comparing(YandexSpellerCheckResponse::pos).reversed())
                .forEach(c -> correctedText.replace(
                        c.pos(),
                        c.pos() + c.len(),
                        c.s().getFirst()
                ));

        return correctedText.toString();
    }
}
