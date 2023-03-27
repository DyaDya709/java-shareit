package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class User {
    Long id;
    String email;
    String name;
}
