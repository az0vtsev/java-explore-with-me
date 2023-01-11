package ru.practicum.ewm.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService service;

    @Autowired
    public PublicCategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                    @Positive @RequestParam(defaultValue = "10") int size) {
    log.info("GET /categories?from={}&size={} request received", from, size);
        return service.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    CategoryDto getCategory(@Positive @PathVariable int catId) {
    log.info("GET /categories/{} request received", catId);
        return service.getCategory(catId);
    }

}
