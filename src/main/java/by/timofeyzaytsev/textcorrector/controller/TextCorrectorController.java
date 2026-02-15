package by.timofeyzaytsev.textcorrector.controller;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextCorrectionRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateTextCorrectionResponse;
import by.timofeyzaytsev.textcorrector.service.TextCorrectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text/corrections")
public class TextCorrectorController {

  private final TextCorrectorService textCorrectorService;

  @PostMapping
  public CreateTextCorrectionResponse createTextCorrection(@Valid @RequestBody CreateTextCorrectionRequest request) {
      return textCorrectorService.createTextCorrection(request);
  }
}
