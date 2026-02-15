package by.timofeyzaytsev.textcorrector.scheduler;

import by.timofeyzaytsev.textcorrector.dto.request.YandexSpellCheckRequest;
import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus;
import by.timofeyzaytsev.textcorrector.mapper.CorrectionTaskMapper;
import by.timofeyzaytsev.textcorrector.repository.CorrectionTaskRepository;
import by.timofeyzaytsev.textcorrector.service.YandexSpellService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CorrectionTaskScheduler {

    private final YandexSpellService yandexSpellService;
    private final CorrectionTaskRepository correctionTaskRepository;
    private final CorrectionTaskMapper correctionTaskMapper;
    private final static int IGNORE_DIGITS = 2;
    private final static int IGNORE_URLS = 4;
    private final static int IGNORE_URLS_AND_DIGITS = 6;

    @Scheduled(fixedRate = 5000)
    public void processTexts() {
        List<CorrectionTask> correctionTasks = correctionTaskRepository.findByStatus();

        if (correctionTasks.isEmpty())
            return;

        for (CorrectionTask task : correctionTasks) {
            int options = calculateOptions(task.getText());
            checkAndChangeStatus(task);
            YandexSpellCheckRequest request = correctionTaskMapper.toYandexSpellCheckRequest(task, List.of(task.getText()), options);
            yandexSpellService.checkAndCorrectText(task, request);
            correctionTaskRepository.save(task);
        }
    }

    private void checkAndChangeStatus(CorrectionTask task) {
        if (task.getStatus().equals(CorrectionTaskStatus.NEW_TASK)) {
            task.setStatus(CorrectionTaskStatus.PROCCESSING);
            correctionTaskRepository.save(task);
        }
    }

    private int calculateOptions(String text) {
        boolean hasDigits = containsDigits(text);
        boolean hasUrl = containsUrl(text);

        if (hasDigits && hasUrl) {
            return IGNORE_URLS_AND_DIGITS;
        } else if (hasDigits) {
            return IGNORE_DIGITS;
        } else if (hasUrl) {
            return IGNORE_URLS;
        }
        return 0;
    }

    private boolean containsDigits(String text) {
        return text != null && text.matches(".*\\d.*");
    }

    private boolean containsUrl(String text) {
        if (text == null) return false;

        // Простая проверка на наличие URL
        return text.contains("http://") ||
                text.contains("https://") ||
                text.contains("www.") ||
                text.matches(".*\\.[a-zA-Z]{2,3}([/\\s].*|$)");
    }
}
