package by.timofeyzaytsev.textcorrector.scheduler;

import by.timofeyzaytsev.textcorrector.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChangeStatusToProcessingScheduler {

    private final TextPolishTaskRepository textPolishTaskRepository;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void schedulePolishTask() {
        List<TextPolishTask> tasks =
                textPolishTaskRepository.findAllNewTasks(PageRequest.of(0, 10));

        tasks.stream().parallel().forEach(task ->
            task.setStatus(TextPolishTaskStatus.PROCESSING)
        );
    }
}
