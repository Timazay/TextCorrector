package by.timofeyzaytsev.textcorrector.controller.textpolish;

import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskHandler;
import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text-polish-tasks")
public class FindTextPolishTaskController {

    private final FindTextPolishTaskHandler handler;

    @GetMapping("/{taskId}")
    public FindTextPolishTaskResponse findTextPolishTask(@PathVariable UUID taskId) {
        return handler.execute(taskId);
    }
}
