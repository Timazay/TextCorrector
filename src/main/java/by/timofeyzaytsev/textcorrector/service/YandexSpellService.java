package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.dto.request.YandexSpellCheckRequest;
import by.timofeyzaytsev.textcorrector.dto.response.YandexSpellCheckResponse;
import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus;
import by.timofeyzaytsev.textcorrector.feignclient.ExternalYandexSpellerApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class YandexSpellService {

    private final ExternalYandexSpellerApiClient externalYandexSpellerApiClient;
    private static final int MAX_TEXT_LENGTH = 10000;

    public void checkAndCorrectText(CorrectionTask task, YandexSpellCheckRequest request) {
        List<List<YandexSpellCheckResponse>> responses =
                externalYandexSpellerApiClient.checkText(request.text().getFirst(), request.lang());

        for (List<YandexSpellCheckResponse> response : responses) {
            if (!responses.isEmpty()) {
                processResponse(task, response);
                return;
            }

            task.setStatus(CorrectionTaskStatus.FINISHED);
        }
    }

    private void processResponse(CorrectionTask task, List<YandexSpellCheckResponse> corrections) {
        try {
            StringBuilder correctedText = new StringBuilder(task.getText());

            corrections.stream()
                    .filter(c -> c.s() != null && !c.s().isEmpty())
                    .sorted(Comparator.comparing(YandexSpellCheckResponse::pos).reversed())
                    .forEach(c -> correctedText.replace(
                            c.pos(),
                            c.pos() + c.len(),
                            c.s().getFirst()
                    ));

            task.setText(correctedText.toString());
            task.setStatus(CorrectionTaskStatus.FINISHED);
        } catch (Exception e) {
            log.error("Error processing Yandex API response", e);
            task.setStatus(CorrectionTaskStatus.FAILED);
        }
    }
}
