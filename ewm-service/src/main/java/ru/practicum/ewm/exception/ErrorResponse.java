package ru.practicum.ewm.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    @NonNull
    private List<String> errors;
    @NonNull
    private String message;
    @NonNull
    private String reason;
    @NonNull
    private String status;
    @NonNull
    private String timestamp;
}
