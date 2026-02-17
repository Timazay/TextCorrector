package by.timofeyzaytsev.textcorrector.common.exception;

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
                message,
                errorCode,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()
        );
    }
}
