package by.timofeyzaytsev.textcorrector.features.textpolish.find_task;

import by.timofeyzaytsev.textcorrector.common.exception.NotFoundException;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.infrastructure.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTextPolishTaskHandler {

    private final TextPolishTaskRepository textPolishTaskRepository;
    private final FindTextPolishTaskMapper findTextPolishTaskMapper;

    public FindTextPolishTaskResponse execute(UUID id) {
        TextPolishTask task = textPolishTaskRepository.findCorrectionTaskById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));

        if (!task.getStatus().equals(TextPolishTaskStatus.FINISHED))
            task.setText(null);

        return findTextPolishTaskMapper.toFindCorrectionTaskResponse(task);
    }
}
