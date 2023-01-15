package ru.practicum.ewm.exception;

public class NotValidDataException extends RuntimeException {
    public NotValidDataException(String message) {
        super(message);
    }
}
