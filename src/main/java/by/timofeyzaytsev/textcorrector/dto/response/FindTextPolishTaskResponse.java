package by.timofeyzaytsev.textcorrector.dto.response;

import by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FindTextPolishTaskResponse(
        String text,
        TextPolishTaskStatus status,
        String errorDescription
) {
}
