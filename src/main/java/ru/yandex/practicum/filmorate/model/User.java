package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;

    @Email(message = "Invalid email format")
    private final String email;

    @NotBlank(message = "Login must not be empty")
    private final String login;

    private String name;

    @PastOrPresent(message = "Date of birth must not be in the future")
    private LocalDate birthday;
}
