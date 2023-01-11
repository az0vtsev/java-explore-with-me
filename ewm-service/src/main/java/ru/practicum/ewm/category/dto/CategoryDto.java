package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class CategoryDto {
    @Positive
    private Integer id;
    @NotBlank(message = "Category name is required")
    private String name;
}
