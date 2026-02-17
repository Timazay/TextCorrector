package by.timofeyzaytsev.textcorrector.features.textpolish.task_polish;

import by.timofeyzaytsev.textcorrector.features.textpolish.common.StringUtils;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.infrastructure.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class ToPolishTaskScheduler {

    private final ToPolishTextHandler handler;
    private final TextPolishTaskRepository textPolishTaskRepository;
    private final ToPolishTaskMapper toPolishTaskMapper;

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
            String text = handler.execute(task,
                    StringUtils.containsDigits(task.getText()), StringUtils.containsUrl(task.getText()));
            toPolishTaskMapper.toCorrectionTask(task, text);
        } catch (Exception e) {
            log.error("Error processing Yandex API response, task with id: {}", task.getId(), e);
            task.setErrorDescription("Error processing Yandex API response: " + e.getMessage());
            task.setStatus(TextPolishTaskStatus.FAILED);
        }
        textPolishTaskRepository.save(task);
    }
}
