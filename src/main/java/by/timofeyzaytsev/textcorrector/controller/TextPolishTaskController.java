package by.timofeyzaytsev.textcorrector.controller;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.dto.response.FindTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.service.TextPolishTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text-polish-tasks")
public class TextPolishTaskController {

    private final TextPolishTaskService textPolishTaskService;

    @PostMapping
    public CreateTextPolishTaskResponse createCorrectionTask(@Valid @RequestBody CreateTextPolishTaskRequest request) {
        return textPolishTaskService.createCorrectionTask(request);
    }

    @GetMapping("/{taskId}")
    public FindTextPolishTaskResponse findCorrectionTask(@PathVariable UUID taskId) {
        return textPolishTaskService.findCorrectionTask(taskId);
    }
}
