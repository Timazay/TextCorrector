package by.timofeyzaytsev.textpolish.features.textpolish.create_task;

import by.timofeyzaytsev.textpolish.infrastructure.entity.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTextPolishTaskRequest(
        @Schema(
                description = "Text to be polished",
                example = "Hallo world! How ara you doing?"
        )
        @NotBlank(message = "Text cannot be empty")
        @Size(min = 3, message = "Text must have at least 3 symbols")
        @Pattern(
                regexp = ".*\\p{L}.*",
                flags = Pattern.Flag.DOTALL,
                message = "Text must contain at least one letter"
        )
        String text,

        @NotNull(message = "Language cannot be null")
        @Schema(
                description = "Language of the text",
                example = "EN",
                allowableValues = {"EN", "RU"}
        )
        Language language
) {
}
