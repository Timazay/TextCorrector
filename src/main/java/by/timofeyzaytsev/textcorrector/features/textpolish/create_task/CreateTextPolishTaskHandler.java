package by.timofeyzaytsev.textcorrector.features.textpolish.create_task;

import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textcorrector.infrastructure.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTextPolishTaskHandler {

    private final TextPolishTaskRepository textPolishTaskRepository;
    private final CreateTextPolishTaskMapper createTextPolishTaskMapper;

    public CreateTextPolishTaskResponse execute(CreateTextPolishTaskRequest request) {
        TextPolishTask textPolishTask = createTextPolishTaskMapper.toCorrectionTask(request);
        textPolishTaskRepository.save(textPolishTask);
        return new CreateTextPolishTaskResponse(textPolishTask.getId());
    }
}
