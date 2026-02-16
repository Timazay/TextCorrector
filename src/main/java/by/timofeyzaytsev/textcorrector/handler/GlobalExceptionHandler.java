package by.timofeyzaytsev.textcorrector.handler;

import by.timofeyzaytsev.textcorrector.dto.common.ErrorResponseDto;
import by.timofeyzaytsev.textcorrector.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleBadRequests(Exception e) {
        log.error("Bad Requests error: {}", e.getMessage(), e);
        return new ErrorResponseDto("400", e.getMessage());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponseDto("400", ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({
            NotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleNotFoundException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponseDto("404", ex.getMessage());
    }
}
