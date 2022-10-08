package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleValidationException(ValidationException e) {
        log.warn("ValidationException", e);
        return new ErrorResponse(400, "Bad Request",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleRedoCreationException(RedoCreationException e) {
        log.warn("RedoCreationException", e);
        return new ErrorResponse(400, "Bad Request",e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleObjectNotFoundException(ObjectNotFoundException e) {
        log.warn("ObjectNotFoundException", e);
        return new ErrorResponse(404, "Not Found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.warn("Throwable", e);
        return new ErrorResponse(500, "Internal Server Error", "Произошла непредвиденная ошибка.");
    }
}
