package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextCorrectionRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateTextCorrectionResponse;
import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import by.timofeyzaytsev.textcorrector.mapper.CorrectionTaskMapper;
import by.timofeyzaytsev.textcorrector.repository.CorrectionTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextCorrectorService {

    private final CorrectionTaskRepository correctionTaskRepository;
    private final CorrectionTaskMapper correctionTaskMapper;

    public CreateTextCorrectionResponse createTextCorrection(CreateTextCorrectionRequest request) {
        CorrectionTask correctionTask = correctionTaskMapper.toCorrectionTask(request);
        correctionTaskRepository.save(correctionTask);
        return new CreateTextCorrectionResponse(correctionTask.getId());
    }
}
