package by.timofeyzaytsev.textpolish.features.textpolish.create_task;

import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import by.timofeyzaytsev.textpolish.infrastructure.repository.TextPolishTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTextPolishTaskHandler {

    private final TextPolishTaskRepository textPolishTaskRepository;
    private final CreateTextPolishTaskMapper createTextPolishTaskMapper;

    public CreateTextPolishTaskResponse execute(CreateTextPolishTaskRequest request) {
        TextPolishTask textPolishTask = createTextPolishTaskMapper.toTextPolishTask(request);
        textPolishTaskRepository.save(textPolishTask);
        return new CreateTextPolishTaskResponse(textPolishTask.getId());
    }
}
