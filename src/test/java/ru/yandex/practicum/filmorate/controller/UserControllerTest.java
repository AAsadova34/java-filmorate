package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    public void addUserWithoutIdTest() {
        User user = User.builder()
                .email("name@yandex.ru")
                .login("user1")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertEquals(1, userController.addUser(user).getId(), "Не сгенерирован id");
    }

    @Test
    public void addUseAgainTest() {
        User user1 = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("user1")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User user2 = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("user1")
                .name("Petr")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userController.addUser(user1);

        RedoCreationException e = Assertions.assertThrows(
                RedoCreationException.class, () -> userController.addUser(user2));
        assertEquals("User already exists", e.getMessage());
    }

    @Test
    public void addUserWithInvalidLoginFormatTest() {
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("Ivan Ivanov")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        ValidationException e = Assertions.assertThrows(
                ValidationException.class, () -> userController.addUser(user));

        assertEquals("Login must not contain spaces",
                e.getMessage());
    }

    @Test
    public void addUserWithNullNameTest() {
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("Ivan_Ivanov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        assertEquals("Ivan_Ivanov", userController.addUser(user).getName(),
                "login и name не совпадают");
    }

    @Test
    public void addUserWithEmptyNameTest() {
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("Ivan_Ivanov")
                .name(" ")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        assertEquals("Ivan_Ivanov", userController.addUser(user).getName(),
                "login и name не совпадают");
    }

    @Test
    public void addUserWithNullBirthdayTest() {
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("Ivan_Ivanov")
                .name("Ivan")
                .build();

        assertNull(userController.addUser(user).getBirthday(),
                "Не создан пользователь без даты рождения");
    }

    @Test
    public void updateUserTest() {
        User user1 = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("user1")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userController.addUser(user1);
        User user2 = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("user1")
                .name("newIvan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userController.updateUser(user2);
        assertFalse(userController.getUsers().contains(user1), "Пользователь не обновлен");
        assertTrue(userController.getUsers().contains(user2), "Пользователь не обновлен");
    }

   @Test
    public void updateAnUncreatedUserTest() {
       User user1 = User.builder()
               .id(1)
               .email("name@yandex.ru")
               .login("user1")
               .name("Ivan")
               .birthday(LocalDate.of(1990, 1, 1))
               .build();
       ObjectNotFoundException e = Assertions.assertThrows(
               ObjectNotFoundException.class, () -> userController.updateUser(user1));
       assertEquals("User with id 1 not found", e.getMessage());
   }
}