package ru.practicum.ewm.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.NewUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<UserDto> users;
        if (ids.isEmpty() || isIdsDefault(ids)) {
            users = repository.findAll(pageRequest)
                    .stream()
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
        } else {
            List<Integer> validIds = ids.stream().filter(id -> id > 0).collect(Collectors.toList());
            users = repository.findAllByIds(validIds, pageRequest)
                    .stream()
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
        }
        return users;
    }

    @Override
    public UserDto createUser(NewUserDto userDto) {
        User newUser = repository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public void deleteUser(int userId) {
        if (repository.findById(userId).isEmpty()) {
            throw new NotFoundException("User id=" + userId + " was not found");
        }
        repository.deleteById(userId);
    }

    private boolean isIdsDefault(List<Integer> ids) {
        return (ids.size() == 1 && ids.get(0) == 0);
    }
}
