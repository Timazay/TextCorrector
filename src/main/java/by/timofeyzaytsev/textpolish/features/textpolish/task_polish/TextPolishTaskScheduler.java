package by.timofeyzaytsev.textpolish.features.textpolish.task_polish;

import by.timofeyzaytsev.textpolish.features.textpolish.common.StringUtils;
import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textpolish.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textpolish.infrastructure.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scheduler for automatic processing of text polishing tasks.
 * Runs every 5 seconds, receives tasks with PROCESSING status, and executes them in parallel.
 * In case of an error, increments the retry counter, and if the maximum number (MAX_COUNT) is exceeded, set tasks to FAILED.
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class TextPolishTaskScheduler {

    private final ProcessTextPolishTaskHandler handler;
    private final TextPolishTaskRepository textPolishTaskRepository;
    private final TextPolishTaskMapper textPolishTaskMapper;
    private final ChangeStatusToProcessing changeStatusToProcessing;
    private final static int MAX_COUNT = 3;

    @Scheduled(fixedRate = 5000)
    public void processTexts() {
        List<TextPolishTask> textPolishTasks = changeStatusToProcessing.change();

        if (textPolishTasks.isEmpty())
            return;

        textPolishTasks.stream().parallel().forEach(this::processSingleTask);
    }

    private void processSingleTask(TextPolishTask task) {
        try {
            String text = handler.execute(task, StringUtils
                    .containsDigits(task.getText()), StringUtils.containsUrl(task.getText()));
            textPolishTaskMapper.toFinishTask(task, text);
        } catch (Exception e) {
            log.error("Error processing Yandex API response, task with id: {}", task.getId(), e);
            if (task.getCount() > MAX_COUNT) {
                task.setErrorDescription("Error processing Yandex API response: " + e.getMessage());
                task.setStatus(TextPolishTaskStatus.FAILED);
            }
        }
        textPolishTaskRepository.save(task);
    }
}
