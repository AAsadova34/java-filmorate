package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    @PositiveOrZero(message = "id must not be negative")
    private long id;

    @Email(message = "Invalid email format")
    private final String email;

    @NotBlank(message = "Login must not be empty")
    private final String login;

    private String name;

    @PastOrPresent(message = "Date of birth must not be in the future")
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();

    private final Set<Long> friendRequests = new HashSet<>();
}
