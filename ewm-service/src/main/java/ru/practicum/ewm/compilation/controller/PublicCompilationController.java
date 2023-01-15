package ru.practicum.ewm.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService service;

    @Autowired
    public PublicCompilationController(CompilationService service) {
        this.service = service;
    }

    @GetMapping
    List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
    log.info("GET /compilations?pinned={}&from={}&size={} request received", pinned, from, size);
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    CompilationDto getCompilation(@Positive @PathVariable int compId) {
    log.info("GET /compilations/{} request received", compId);
        return service.getCompilation(compId);
    }
}
