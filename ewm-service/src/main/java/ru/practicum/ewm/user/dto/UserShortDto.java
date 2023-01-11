package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserShortDto {
    private final int id;
    @NotBlank(message = "User name is required")
    private String name;
}
