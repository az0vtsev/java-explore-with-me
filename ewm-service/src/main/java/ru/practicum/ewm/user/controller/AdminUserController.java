package ru.practicum.ewm.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService service;

    @Autowired
    public AdminUserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(defaultValue = "0") List<Integer> ids,
                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Positive @RequestParam(defaultValue = "10") int size) {
    log.info("GET /admin/users?ids={}&from={}&size={} request received", ids, from, size);
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody NewUserDto userDto) {
    log.info("POST /admin/users request received, request body={}", userDto);
        return service.createUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@Positive @PathVariable int userId) {
    log.info("DELETE /admin/users/{} request received", userId);
        service.deleteUser(userId);
    }
}
