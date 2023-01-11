package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class NewUserDto {
    @NotNull
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Must be formatted: mailName@domain")
    private String email;
    @NotNull
    @NotBlank(message = "User name is required")
    private String name;
}
