package by.timofeyzaytsev.textcorrector.dto.response;

import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FindCorrectionTaskResponse(
        String text,
        CorrectionTaskStatus status
) {
}
