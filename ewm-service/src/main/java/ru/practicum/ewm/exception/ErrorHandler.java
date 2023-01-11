package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.category.controller.AdminCategoryController;
import ru.practicum.ewm.category.controller.PublicCategoryController;
import ru.practicum.ewm.compilation.controller.AdminCompilationController;
import ru.practicum.ewm.compilation.controller.PublicCompilationController;
import ru.practicum.ewm.event.controller.AdminEventController;
import ru.practicum.ewm.event.controller.PrivateEventController;
import ru.practicum.ewm.event.controller.PublicEventController;
import ru.practicum.ewm.request.controller.PrivateRequestController;
import ru.practicum.ewm.user.controller.AdminUserController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(assignableTypes = {AdminUserController.class, AdminCategoryController.class,
        PublicCategoryController.class, PublicCompilationController.class, PublicEventController.class,
        AdminCompilationController.class, PrivateEventController.class, AdminEventController.class,
        PrivateRequestController.class})
public class ErrorHandler {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Entity not found.",
                "NOT_FOUND",
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now())
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestParameterException(final MissingServletRequestParameterException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Request parameter is missing.",
                "BAD_REQUEST",
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now())
        );
    }



    @ExceptionHandler(NotValidDataException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleNotValidDataException(final NotValidDataException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                "FORBIDDEN",
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now())
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getMessage(),
                "Method argument not valid.",
                "BAD_REQUEST",
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now())
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(final DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
               Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
               e.getMessage(),
               "Integrity constraint has been violated.",
               "CONFLICT",
               DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now())
            );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()),
                e.getClass().getName(),
                "Error occurred.",
                "INTERNAL_SERVER_ERROR",
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now())
        );
    }

}
