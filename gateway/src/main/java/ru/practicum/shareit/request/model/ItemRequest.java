package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.response.model.ItemResponse;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class ItemRequest {
    private Long id;
    private Long userId;
    private String description;
    private Instant created;
    private Set<ItemResponse> responses;

}
