package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    @NotNull(message = "Compilation events list is required")
    private List<Integer> events;
    private Boolean pinned;
    @NotNull
    @NotBlank(message = "Compilation title is required")
    private String title;
}
