package by.timofeyzaytsev.textcorrector.controller.textpolish;

import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskHandler;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text-polish-tasks")
public class CreateTextPolishTaskController {

    private final CreateTextPolishTaskHandler handler;

    @PostMapping
    public CreateTextPolishTaskResponse createTextPolishTask(@Valid @RequestBody CreateTextPolishTaskRequest request) {
        return handler.execute(request);
    }
}
