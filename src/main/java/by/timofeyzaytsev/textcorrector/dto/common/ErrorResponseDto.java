package by.timofeyzaytsev.textcorrector.dto.common;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ErrorResponseDto(
        String message,
        String errorCode,
        String timestamp,
        String path
) {
    public ErrorResponseDto(String errorCode, String message) {
        this(
                ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString(),
                errorCode,
                message,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
