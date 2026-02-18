package by.timofeyzaytsev.textpolish.features.textpolish.task_polish;

import by.timofeyzaytsev.textpolish.infrastructure.client.CallYandexSpellerClient;
import by.timofeyzaytsev.textpolish.infrastructure.client.YandexSpellerCheckResponse;
import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static by.timofeyzaytsev.textpolish.features.textpolish.common.StringUtils.splitIntoBlocks;


@Service
@RequiredArgsConstructor
public class ProcessTextPolishTaskHandler {

    private final CallYandexSpellerClient callYandexSpellerClient;
    private static final int MAX_TEXT_LENGTH = 10000;

    /**
     * The method sends a request to the post checkText method in parts (if the string exceeds MAX_TEXT_LENGTH characters)
     *
     * @return returns a string with the corrected text.
     */
    public String execute(TextPolishTask task, boolean ignoreDigits, boolean ignoreUrls) {
        List<String> blocks = splitIntoBlocks(task.getText(), MAX_TEXT_LENGTH);
        return blocks.stream().map(textBlock -> {
            List<List<YandexSpellerCheckResponse>> responses = callYandexSpellerClient
                            .execute(textBlock, task.getLanguage().name(), ignoreDigits, ignoreUrls);

            List<YandexSpellerCheckResponse> flatResponses = responses.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            return processResponse(textBlock, flatResponses);
        }).collect(Collectors.joining());
    }

    /**
     * Method for correcting text based on a Yandex API response
     *
     * @param task        : original text
     * @param corrections : list of corrections from the API
     * @return : corrected text
     */
    private String processResponse(String task, List<YandexSpellerCheckResponse> corrections) {
        StringBuilder correctedText = new StringBuilder(task);

        corrections.stream()
                .filter(c -> c.s() != null && !c.s().isEmpty())
                .sorted(Comparator.comparing(YandexSpellerCheckResponse::pos).reversed())
                .forEach(c ->
                        correctedText.replace(c.pos(), c.pos() + c.len(), c.s().getFirst()));

        return correctedText.toString();
    }
}
