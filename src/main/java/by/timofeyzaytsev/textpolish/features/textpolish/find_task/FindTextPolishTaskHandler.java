package by.timofeyzaytsev.textpolish.features.textpolish.find_task;

import by.timofeyzaytsev.textpolish.common.exception.NotFoundException;
import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textpolish.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textpolish.infrastructure.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTextPolishTaskHandler {

    private final TextPolishTaskRepository textPolishTaskRepository;
    private final FindTextPolishTaskMapper findTextPolishTaskMapper;

    public FindTextPolishTaskResponse execute(UUID id) {
        TextPolishTask task = textPolishTaskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));

        if (!task.getStatus().equals(TextPolishTaskStatus.FINISHED))
            task.setText(null);

        return findTextPolishTaskMapper.toFindTextPolishTaskResponse(task);
    }
}
