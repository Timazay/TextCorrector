package by.timofeyzaytsev.textcorrector.controller;

import by.timofeyzaytsev.textcorrector.dto.request.CreateCorrectionTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.dto.response.FindCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.service.TextCorrectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text/corrections")
public class TextCorrectorController {

    private final TextCorrectorService textCorrectorService;

    @PostMapping
    public CreateCorrectionTaskResponse createTextCorrection(@Valid @RequestBody CreateCorrectionTaskRequest request) {
        return textCorrectorService.createCorrectionTask(request);
    }

    @GetMapping
    public FindCorrectionTaskResponse findTextCorrection(@RequestParam UUID id) {
        return textCorrectorService.findCorrectionTask(id);
    }
}
