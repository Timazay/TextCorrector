package by.timofeyzaytsev.textcorrector.dto.request;

import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTextCorrectionRequest(
        @NotBlank(message = "Text cannot be empty")
        @Size(min = 3, message = "Text must have at least 3 symbols")
        @Pattern(
                regexp = ".*\\p{L}.*",
                message = "Text must contain at least one letter"
        )
        String text,
        CorrectionTaskLanguage language
) {
}
