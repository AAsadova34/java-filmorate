package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();

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
    public void addUserWithNegativeId() {
        User user = User.builder()
                .id(-1)
                .email("name@yandex.ru")
                .login("user1")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException e = Assertions.assertThrows(
                ValidationException.class, () -> userController.addUser(user));
        assertEquals("id must not be negative", e.getMessage());
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
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("user1")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userController.updateUser(user);
        assertTrue(userController.getUsers().contains(user),
                "Пользователь не создан");
    }

    @Test
    public void updateUserWithoutIdTest() {
        User user = User.builder()
                .email("name@yandex.ru")
                .login("user1")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertEquals(1, userController.updateUser(user).getId(),
                "Не сгенерирован id");
    }

    @Test
    public void updateUseAgainTest() {
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
        userController.updateUser(user1);
        userController.updateUser(user2);

        assertFalse(userController.getUsers().contains(user1) , "Пользователь не обновлен");
        assertTrue(userController.getUsers().contains(user2) , "Пользователь не обновлен");
    }

    @Test
    public void updateUserWithInvalidLoginFormatTest() {
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("Ivan Ivanov")
                .name("Ivan")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        ValidationException e = Assertions.assertThrows(
                ValidationException.class, () -> userController.updateUser(user));

        assertEquals("Login must not contain spaces",
                e.getMessage());
    }

    @Test
    public void updateUserWithNullNameTest() {
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("Ivan_Ivanov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        assertEquals("Ivan_Ivanov", userController.updateUser(user).getName(),
                "login и name не совпадают");
    }

    @Test
    public void updateUserWithEmptyNameTest() {
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("Ivan_Ivanov")
                .name(" ")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        assertEquals("Ivan_Ivanov", userController.updateUser(user).getName(),
                "login и name не совпадают");
    }

    @Test
    public void updateUserWithNullBirthdayTest() {
        User user = User.builder()
                .id(1)
                .email("name@yandex.ru")
                .login("Ivan_Ivanov")
                .name("Ivan")
                .build();
        assertNull(userController.updateUser(user).getBirthday(),
                "Не создан пользователь без даты рождения");
    }
}