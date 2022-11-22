package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping//получить полный список фильмов
    public Collection<Film> getFilms() {
        Logger.logRequest(HttpMethod.GET, "/films", "no body");
        return filmService.getFilms();
    }

    @PostMapping//добавить фильм
    public Film addFilm(@Valid @RequestBody Film film) {
        Logger.logRequest(HttpMethod.POST, "/films", film.toString());
        return filmService.addFilm(film);
    }

    @PutMapping//обновить фильм или добавить фильм (если он не был добавлен ранее)
    public Film updateFilm(@Valid @RequestBody Film film) {
        Logger.logRequest(HttpMethod.PUT, "/films", film.toString());
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}") //получить фильм по id
    public Film getFilmById(@PathVariable long id) {
        Logger.logRequest(HttpMethod.GET, "/films/" + id, "no body");
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}")
    public void removeFilmById(@PathVariable long id) {
        Logger.logRequest(HttpMethod.DELETE, "/films/" + id, "no body");
        filmService.removeFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")//поставить лайк
    public void addLike(@PathVariable long id,
                           @PathVariable long userId) {
        Logger.logRequest(HttpMethod.PUT, "/films/" + id + "/like/" + userId, "no body");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")//снять лайк
    public void unlike(@PathVariable long id,
                          @PathVariable long userId) {
        Logger.logRequest(HttpMethod.DELETE, "/films/" + id + "/like/" + userId, "no body");
        filmService.unlike(id, userId);
    }

    @GetMapping("/{id}/likes")//получить список id пользователей, поставивших лайк
    public List<Long> getListOfLikes(@PathVariable long id) {
        Logger.logRequest(HttpMethod.GET, "/films/" + id + "/likes", "no body");
        return filmService.getListOfLikes(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularByGenreAndYear( //получение count списка фильмов по жанру и году
                                                @RequestParam(defaultValue = "10") @Positive int count,
                                                @RequestParam(required = false) @Positive Integer genreId,
                                                @RequestParam(required = false) Integer year) {
        Logger.logRequest(HttpMethod.GET, "/films/popular?count=" + count +
                "&genreId=" + genreId + "&year=" + year, "no body");
        return filmService.getByGenreAndYear(count, genreId, year);
    }
}
