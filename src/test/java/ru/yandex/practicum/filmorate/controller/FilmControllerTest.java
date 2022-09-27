package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    public void addFilmWithoutIdTest() {
        Film film = Film.builder()
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();

        assertEquals(1, filmController.addFilm(film).getId(), "Не сгенерирован id");
    }

    @Test
    public void addFilmAgainTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        Film film2 = Film.builder()
                .id(1)
                .name("Psiconautas")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.addFilm(film1);

        RedoCreationException e = Assertions.assertThrows(
                RedoCreationException.class, () -> filmController.addFilm(film2));
        assertEquals("Movie already exists", e.getMessage());
    }

    @Test
    public void addAFilmWithNullDescriptionTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.addFilm(film);
        assertTrue(filmController.getFilms().contains(film), "Не добавлен фильм без описания");
    }

    @Test
    public void addFilmWithAnIncorrectReleaseDateTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(109)
                .build();

        ValidationException e = Assertions.assertThrows(
                ValidationException.class, () -> filmController.addFilm(film));

        assertEquals("Release date must not be earlier than 12-28-1895", e.getMessage());
    }

    @Test
    public void addFilmWithNullReleaseDateTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .duration(109)
                .build();
        filmController.addFilm(film);
        assertTrue(filmController.getFilms().contains(film), "Не добавлен фильм без даты релиза");
    }

    @Test
    public void updateFilmWithoutIdTest() {
        Film film = Film.builder()
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();

        assertEquals(1, filmController.updateFilm(film).getId(), "Не сгенерирован id");
    }

    @Test
    public void updateFilmAgainTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        Film film2 = Film.builder()
                .id(1)
                .name("Psiconautas")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.updateFilm(film1);
        filmController.updateFilm(film2);

        assertFalse(filmController.getFilms().contains(film1), "Пользователь не обновлен");
        assertTrue(filmController.getFilms().contains(film2), "Пользователь не обновлен");
    }

    @Test
    public void updateAFilmWithNullDescriptionTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.updateFilm(film);
        assertTrue(filmController.getFilms().contains(film), "Не добавлен фильм без описания");
    }

    @Test
    public void updateFilmWithAnIncorrectReleaseDateTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(109)
                .build();

        ValidationException e = Assertions.assertThrows(
                ValidationException.class, () -> filmController.updateFilm(film));

        assertEquals("Release date must not be earlier than 12-28-1895", e.getMessage());
    }

    @Test
    public void updateFilmWithNullReleaseDateTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .duration(109)
                .build();
        filmController.updateFilm(film);
        assertTrue(filmController.getFilms().contains(film), "Не добавлен фильм без даты релиза");
    }
}