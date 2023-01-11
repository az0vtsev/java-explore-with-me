package ru.practicum.ewm.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@Validated
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService service;

    @Autowired
    public AdminCompilationController(CompilationService service) {
        this.service = service;
    }

    @PostMapping
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
    log.info("POST /admin/compilations request received, request body={}", compilationDto);
        return service.createCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@Positive @PathVariable int compId) {
    log.info("DELETE /admin/compilations/{} request received", compId);
        service.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEvent(@Positive @PathVariable int compId,
                            @Positive @PathVariable int eventId) {
    log.info("DELETE /admin/compilations/{}/events/{} request received", compId, eventId);
        service.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEvent(@Positive @PathVariable int compId,
                         @Positive @PathVariable int eventId) {
    log.info("PATCH /admin/compilations/{}/events/{} request received", compId, eventId);
        service.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@Positive @PathVariable int compId) {
    log.info("DELETE /admin/compilations/{}/pin request received", compId);
        service.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@Positive @PathVariable int compId) {
    log.info("PATCH /admin/compilations/{}/pin request received", compId);
        service.pinCompilation(compId);
    }

}
