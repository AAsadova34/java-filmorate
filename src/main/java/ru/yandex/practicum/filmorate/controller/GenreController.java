package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping//получить полный список жанров
    public Collection<Genre> getGenres() {
        logRequest(HttpMethod.GET, "/genres", "no body");
        return genreService.getGenres();
    }

    @GetMapping("/{id}") //получить жанр по id
    public Genre getGenreById(@PathVariable int id) {
        logRequest(HttpMethod.GET, "/genres/" + id, "no body");
        return genreService.getGenreById(id);
    }

    private void logRequest(HttpMethod method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }
}
