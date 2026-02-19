package by.timofeyzaytsev.textpolish.features.textpolish.find_task;

import by.timofeyzaytsev.textpolish.infrastructure.entity.enums.TextPolishTaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FindTextPolishTaskResponse(
        String text,
        TextPolishTaskStatus status,
        String errorDescription
) {
}
