package by.timofeyzaytsev.textcorrector.features.textpolish.create_task;

import by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTextPolishTaskRequest(
        @NotBlank(message = "Text cannot be empty")
        @Size(min = 3, message = "Text must have at least 3 symbols")
        @Pattern(
                regexp = ".*\\p{L}.*",
                flags = Pattern.Flag.DOTALL,
                message = "Text must contain at least one letter"
        )
        String text,
        Language language
) {
}
