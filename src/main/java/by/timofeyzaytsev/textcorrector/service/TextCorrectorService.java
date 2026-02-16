package by.timofeyzaytsev.textcorrector.service;

import by.timofeyzaytsev.textcorrector.dto.request.CreateCorrectionTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.response.CreateCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.dto.response.FindCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus;
import by.timofeyzaytsev.textcorrector.mapper.CorrectionTaskMapper;
import by.timofeyzaytsev.textcorrector.repository.CorrectionTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TextCorrectorService {

    private final CorrectionTaskRepository correctionTaskRepository;
    private final CorrectionTaskMapper correctionTaskMapper;

    public CreateCorrectionTaskResponse createCorrectionTask(CreateCorrectionTaskRequest request) {
        CorrectionTask correctionTask = correctionTaskMapper.toCorrectionTask(request);
        correctionTaskRepository.save(correctionTask);
        return new CreateCorrectionTaskResponse(correctionTask.getId());
    }

    public FindCorrectionTaskResponse findCorrectionTask(UUID id) {
        CorrectionTask task = correctionTaskRepository.findCorrectionTaskById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id: " + id + " not found"));

        if (!task.getStatus().equals(CorrectionTaskStatus.FINISHED))
            task.setText(null);

        return correctionTaskMapper.toFindCorrectionTaskResponse(task);
    }
}
