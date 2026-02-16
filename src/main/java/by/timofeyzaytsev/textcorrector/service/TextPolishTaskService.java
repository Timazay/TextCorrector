package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.dto.response.FindTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus;
import by.timofeyzaytsev.textcorrector.exception.NotFoundException;
import by.timofeyzaytsev.textcorrector.mapper.TextPolishTaskMapper;
import by.timofeyzaytsev.textcorrector.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TextPolishTaskService {

    private final TextPolishTaskRepository textPolishTaskRepository;
    private final TextPolishTaskMapper textPolishTaskMapper;

    public CreateTextPolishTaskResponse createCorrectionTask(CreateTextPolishTaskRequest request) {
        TextPolishTask textPolishTask = textPolishTaskMapper.toCorrectionTask(request);
        textPolishTaskRepository.save(textPolishTask);
        return new CreateTextPolishTaskResponse(textPolishTask.getId());
    }

    public FindTextPolishTaskResponse findCorrectionTask(UUID id) {
        TextPolishTask task = textPolishTaskRepository.findCorrectionTaskById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));

        if (!task.getStatus().equals(TextPolishTaskStatus.FINISHED))
            task.setText(null);

        return textPolishTaskMapper.toFindCorrectionTaskResponse(task);
    }
}
