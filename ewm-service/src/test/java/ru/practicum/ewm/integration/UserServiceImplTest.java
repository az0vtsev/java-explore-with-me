package ru.practicum.ewm.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.user.dto.NewUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceImplTest {
    private final UserService service;

    private User user1;
    private User user2;
    private UserDto user1Dto;
    private UserDto user2Dto;

    private NewUserDto user1NewDto;
    private NewUserDto user2NewDto;


    @BeforeEach
    public void createData() {
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2, "name2", "email2@mail.com");
        user1NewDto = new NewUserDto(user1.getEmail(), user1.getName());
        user2NewDto = new NewUserDto(user2.getEmail(), user2.getName());
        user1Dto = new UserDto(user1.getEmail(), 1, user1.getName());
        user2Dto = new UserDto(user2.getEmail(), 2,  user2.getName());
        service.createUser(user1NewDto);
        service.createUser(user2NewDto);
    }

    @Test
    public void shouldReturnUsersByIds() {
        List<Integer> ids = List.of(1,2,3,0, -1);
        List<UserDto> result = service.getUsers(ids, 0, 10);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(user1.getId());
        assertThat(result.get(0).getEmail()).isEqualTo(user1.getEmail());
        assertThat(result.get(1).getId()).isEqualTo(user2.getId());
        assertThat(result.get(1).getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    public void shouldReturnAllUsersIfIdsIsEmpty() {
        List<Integer> ids = List.of();
        List<UserDto> result = service.getUsers(ids, 0, 10);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(user1.getId());
        assertThat(result.get(0).getEmail()).isEqualTo(user1.getEmail());
        assertThat(result.get(1).getId()).isEqualTo(user2.getId());
        assertThat(result.get(1).getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    public void shouldReturnAllUsersIfIdsIsDefault() {
        List<UserDto> result = service.getUsers(List.of(0), 0, 10);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(user1.getId());
        assertThat(result.get(0).getEmail()).isEqualTo(user1.getEmail());
        assertThat(result.get(1).getId()).isEqualTo(user2.getId());
        assertThat(result.get(1).getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    public void shouldReturnUserById() {
        List<Integer> ids = List.of(2);
        List<UserDto> result = service.getUsers(ids, 0, 10);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(user2.getId());
        assertThat(result.get(0).getEmail()).isEqualTo(user2.getEmail());;
    }

    @Test
    public void shouldCreateUser() {
        NewUserDto newUserDto = new NewUserDto("email3@mail.com", "name3");
        service.createUser(newUserDto);
        List<Integer> ids = List.of();
        List<UserDto> result = service.getUsers(ids, 0, 10);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(2).getId()).isEqualTo(3);
        assertThat(result.get(2).getEmail()).isEqualTo(newUserDto.getEmail());;
    }

    @Test
    public void shouldDeleteUserById() {
        service.deleteUser(2);
        List<Integer> ids = List.of();
        List<UserDto> result = service.getUsers(ids, 0, 10);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(user1.getId());
        assertThat(result.get(0).getEmail()).isEqualTo(user1.getEmail());;
    }

}
