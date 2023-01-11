package ru.practicum.ewm.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Must be formatted: mailName@domain")
    private String email;
    private int id;
    @NotBlank(message = "User name is required")
    private String name;
}
