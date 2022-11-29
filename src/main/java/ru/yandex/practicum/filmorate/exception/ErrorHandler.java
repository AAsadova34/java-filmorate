package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, RedoCreationException.class,
            RuntimeException.class, MethodArgumentNotValidException.class})
    public ErrorResponse handleValidationException(Exception e) {
        log.warn(e.getClass().getSimpleName(), e);
        String message;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException eValidation = (MethodArgumentNotValidException) e;
            message = Objects.requireNonNull(eValidation.getBindingResult().getFieldError()).getDefaultMessage();
        } else {
            message = e.getMessage();
        }
        return new ErrorResponse(400, "Bad Request", message);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleObjectNotFoundException(ObjectNotFoundException e) {
        log.warn("ObjectNotFoundException", e);
        return new ErrorResponse(404, "Not Found", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpRequestMethodNotSupportedException.class})
    public ErrorResponse handleInvalidRequestException(Exception e) {
        log.warn(e.getClass().getSimpleName(), e);
        return new ErrorResponse(404, "Not Found", "Invalid request");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.warn("Throwable", e);
        return new ErrorResponse(500, "Internal Server Error", "Произошла непредвиденная ошибка.");
    }
}
