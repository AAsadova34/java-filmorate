package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getMpa() {
        Collection<Mpa> mpaInStorage = mpaStorage.getMpa();
        logSave(HttpMethod.GET, "/mpa", mpaInStorage.toString());
        return mpaInStorage;
    }

    public Mpa getMpaById(int id) {
        Mpa mpaInStorage = mpaStorage.getMpaById(id);
        logSave(HttpMethod.GET, "/mpa/" + id, mpaInStorage.toString());
        return mpaInStorage;
    }

    private void logSave(HttpMethod method, String uri, String storage) {
        log.info("Endpoint request result: '{} {}'. In storage: '{}'", method, uri, storage);
    }
}
