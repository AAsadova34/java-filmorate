package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmAnnotationTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void addFilmTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void addFilmWithEmptyNameTest() {
        Film film = Film.builder()
                .id(1)
                .name(" ")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(violations.size(), 1);

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("Movie title must not be empty", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals(" ", violation.getInvalidValue());
    }

    @Test
    public void addFilmWithNullNameTest() {
        Film film = Film.builder()
                .id(1)
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(violations.size(), 1);

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("Movie title must not be empty", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    public void addAFilmWithALongDescriptionTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком " +
                        "по сценарию Джозефа Стефано, основанном на одноимённом романе Роберта Блоха. Главные " +
                        "роли в картине исполняют Энтони Перкинс, Джанет Ли, Вера Майлз, Джон Гэвин, Мартин " +
                        "Болсам и Джон Макинтайр.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(violations.size(), 1);

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("Maximum description length - 200 characters", violation.getMessage());
        assertEquals("description", violation.getPropertyPath().toString());
    }

    @Test
    public void addFilmWithIncorrectDurationTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(-1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(violations.size(), 1);

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("Movie duration must be positive", violation.getMessage());
        assertEquals("duration", violation.getPropertyPath().toString());
    }

    @Test
    public void addFilmWithZeroDurationTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(0)
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(violations.size(), 1);

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("Movie duration must be positive", violation.getMessage());
        assertEquals("duration", violation.getPropertyPath().toString());
    }

    @Test
    public void addFilmWithNullMpaTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(violations.size(), 1);

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("Mpa must not be null", violation.getMessage());
        assertEquals("mpa", violation.getPropertyPath().toString());
    }
}
