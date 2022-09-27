package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ErrorHandler.*;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    private void generateId() {
        id++;
    }

    @GetMapping
    public Collection getFilms() {
        logRequest("GET", "/films", "no body");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        logRequest("POST", "/films", film.toString());

        if (film.getId() == 0) {
            generateId();
            film.setId(id);
        }
        if (film.getId() < 0) {
            ValidationException e = new ValidationException("id must not be negative");
            handleValidationException(e);
            throw e;
        }
        if (film.getReleaseDate() != null && film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12,28))) {
            ValidationException e = new ValidationException("Release date must not be earlier than 12-28-1895");
            handleValidationException(e);
            throw e;
        }
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            RedoCreationException e = new RedoCreationException("Movie already exists");
            handleRedoCreationException(e);
            throw e;
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        logRequest("PUT", "/films", film.toString());

        if (film.getId() == 0) {
            generateId();
            film.setId(id);
        }
        if (film.getId() < 0) {
            ValidationException e = new ValidationException("id must not be negative");
            handleValidationException(e);
            throw e;
        }
        if (film.getReleaseDate() != null && film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12,28))) {
            ValidationException e = new ValidationException("Release date must not be earlier than 12-28-1895");
            handleValidationException(e);
            throw e;
        }
        films.put(film.getId(), film);
        return film;
    }

    private void logRequest(String method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }
}
