package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @PositiveOrZero(message = "id must not be negative")
    private int id;

    @NotBlank(message = "Movie title must not be empty")
    private final String name;

    @Size(max = 200, message = "Maximum description length - 200 characters")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Movie duration must be positive")
    private int duration;
}
