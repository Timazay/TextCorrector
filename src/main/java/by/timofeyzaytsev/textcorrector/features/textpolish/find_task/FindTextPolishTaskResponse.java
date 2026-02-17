package by.timofeyzaytsev.textcorrector.features.textpolish.find_task;

import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FindTextPolishTaskResponse(
        String text,
        TextPolishTaskStatus status,
        String errorDescription
) {
}
