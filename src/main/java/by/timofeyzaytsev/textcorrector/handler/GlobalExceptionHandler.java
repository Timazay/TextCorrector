package by.timofeyzaytsev.textcorrector.handler;

import by.timofeyzaytsev.textcorrector.dto.common.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ErrorResponseDto handleBadRequests(Exception e) {
        log.error("Bad Requests error: {}", e.getMessage(), e);
        return new ErrorResponseDto("400", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponseDto("400", ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleConflict(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponseDto("403", ex.getMessage());
    }

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    public ErrorResponseDto handleEntityNotFoundException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponseDto("404", ex.getMessage());
    }
}
