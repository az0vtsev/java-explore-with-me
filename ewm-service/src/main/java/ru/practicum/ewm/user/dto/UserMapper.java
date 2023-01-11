package ru.practicum.ewm.user.dto;

import ru.practicum.ewm.user.model.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }

    public static User mapToUser(NewUserDto newUserDto) {
        return new User(newUserDto.getName(), newUserDto.getEmail());
    }

    public static UserShortDto mapToUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}
