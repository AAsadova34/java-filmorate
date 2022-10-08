package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping//получить полный список фильмов
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping//добавить фильм
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping//обновить фильм или добавить фильм (если он не был добавлен ранее)
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}") //получить фильм по id
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")//поставить лайк
    public List<Long> addLike(@PathVariable long id,
                           @PathVariable long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")//снять лайк
    public List<Long> unlike(@PathVariable long id,
                             @PathVariable long userId) {
        return filmService.unlike(id, userId);
    }

    @GetMapping("/popular") //получить список из первых count фильмов по количеству лайков
    public List<Film> getTheBestFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTheBestFilms(count);
    }
}
