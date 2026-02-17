package by.timofeyzaytsev.textcorrector.features.textpolish.task_processor_initializer;

import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.infrastructure.repository.TextPolishTaskRepository;
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
    public void changeStatus() {
        List<TextPolishTask> tasks =
                textPolishTaskRepository.findAllNewTasks(PageRequest.of(0, 10));

        tasks.stream().parallel().forEach(task ->
            task.setStatus(TextPolishTaskStatus.PROCESSING)
        );
    }
}
