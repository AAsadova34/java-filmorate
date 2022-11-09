package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @GetMapping//получить полный список рейтингов mpa
    public Collection<Mpa> getMpa() {
        logRequest(HttpMethod.GET, "/mpa", "no body");
        return mpaService.getMpa();
    }

    @GetMapping("/{id}") //получить рейтинг mpa по id
    public Mpa getMpaById(@PathVariable int id) {
        logRequest(HttpMethod.GET, "/mpa/" + id, "no body");
        return mpaService.getMpaById(id);
    }

    private void logRequest(HttpMethod method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }
}
