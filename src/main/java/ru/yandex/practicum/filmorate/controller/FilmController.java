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

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection getFilms() {
        logRequest("GET", "/films", "no body");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        logRequest("POST", "/films", film.toString());
        Film verifiedFilm = checkValidation(film);
        if (!films.containsKey(verifiedFilm.getId())) {
            films.put(verifiedFilm.getId(), verifiedFilm);
        } else {
            throw new RedoCreationException("Movie already exists");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        logRequest("PUT", "/films", film.toString());
        Film verifiedFilm = checkValidation(film);
        films.put(verifiedFilm.getId(), verifiedFilm);
        return film;
    }

    private void generateId() {
        id++;
    }

    private Film checkValidation(Film film) {
        if (film.getId() == 0) {
            generateId();
            film.setId(id);
        }
        if (film.getId() < 0) {
            throw new ValidationException("id must not be negative");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date must not be earlier than 12-28-1895");
        }
        return film;
    }

    private void logRequest(String method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }
}
