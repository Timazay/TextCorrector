package by.timofeyzaytsev.textcorrector.scheduler;

import by.timofeyzaytsev.textcorrector.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.mapper.TextPolishTaskMapper;
import by.timofeyzaytsev.textcorrector.repository.TextPolishTaskRepository;
import by.timofeyzaytsev.textcorrector.service.YandexSpellerService;
import by.timofeyzaytsev.textcorrector.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class TextPolishTaskScheduler {

    private final YandexSpellerService yandexSpellerService;
    private final TextPolishTaskRepository textPolishTaskRepository;
    private final TextPolishTaskMapper textPolishTaskMapper;

    @Scheduled(fixedRate = 5000)
    public void processTexts() {
        List<TextPolishTask> textPolishTasks =
                textPolishTaskRepository.findByProcessingStatus(PageRequest.of(0, 10));

        if (textPolishTasks.isEmpty())
            return;

        textPolishTasks.stream().parallel().forEach(this::processSingleTask);
    }

    private void processSingleTask(TextPolishTask task) {
        try {
            String text = yandexSpellerService.checkAndCorrectText(task,
                    StringUtils.containsDigits(task.getText()), StringUtils.containsUrl(task.getText()));
            textPolishTaskMapper.toCorrectionTask(task, text);
        } catch (Exception e) {
            log.error("Error processing Yandex API response, task with id: {}", task.getId(), e);
            task.setErrorDescription("Error processing Yandex API response: " + e.getMessage());
            task.setStatus(TextPolishTaskStatus.FAILED);
        }
        textPolishTaskRepository.save(task);
    }
}
