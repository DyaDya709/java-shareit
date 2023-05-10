package ru.practicum.shareit.request.response.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

@Getter
@Setter
public class ItemResponse {
    private Long id;
    private Item item;
    private ItemRequest itemRequest;
}
