package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping//получить полный список фильмов
    public Collection<Film> getFilms() {
        logRequest(HttpMethod.GET, "/films", "no body");
        return filmService.getFilms();
    }

    @PostMapping//добавить фильм
    public Film addFilm(@Valid @RequestBody Film film) {
        logRequest(HttpMethod.POST, "/films", film.toString());
        return filmService.addFilm(film);
    }

    @PutMapping//обновить фильм или добавить фильм (если он не был добавлен ранее)
    public Film updateFilm(@Valid @RequestBody Film film) {
        logRequest(HttpMethod.PUT, "/films", film.toString());
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}") //получить фильм по id
    public Film getFilmById(@PathVariable long id) {
        logRequest(HttpMethod.GET, "/films/" + id, "no body");
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")//поставить лайк
    public Film addLike(@PathVariable long id,
                           @PathVariable long userId) {
        logRequest(HttpMethod.PUT, "/films/" + id + "/like/" + userId, "no body");
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")//снять лайк
    public Film unlike(@PathVariable long id,
                             @PathVariable long userId) {
        logRequest(HttpMethod.DELETE, "/films/" + id + "/like/" + userId, "no body");
        return filmService.unlike(id, userId);
    }

    @GetMapping("/popular") //получить список из первых count фильмов по количеству лайков
    public List<Film> getTheBestFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        logRequest(HttpMethod.GET, "/films/popular?count=" + count, "no body");
        return filmService.getTheBestFilms(count);
    }

    private void logRequest(HttpMethod method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }
}
