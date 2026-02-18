package by.timofeyzaytsev.textpolish.features.textpolish.task_polish;

import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textpolish.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textpolish.infrastructure.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChangeStatusToProcessing {

    private final TextPolishTaskRepository textPolishTaskRepository;

    @Transactional
    public List<TextPolishTask> change() {
        List<TextPolishTask> tasks = textPolishTaskRepository
                .findNewAndProcessingTasks(PageRequest.of(0, 10));

        tasks.stream()
                .peek(task -> task.setCount(task.getCount() + 1))
                .filter(task -> task.getStatus().equals(TextPolishTaskStatus.NEW_TASK))
                .forEach(task -> {
                    task.setStatus(TextPolishTaskStatus.PROCESSING);
                    task.setExpiration(LocalDateTime.now().plusMinutes(10));
                });

        return tasks;
    }
}
