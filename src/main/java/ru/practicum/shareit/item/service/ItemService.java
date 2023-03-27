package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item create(Long userId, ItemDto itemDto);
    Item update(Long userId, Long itemId, ItemDto itemDto);
    Item get(Long itemId);
    List<Item> getAll(Long userId);
    Boolean remove(Long id);
    List<Item> findAvailable(String text);
}
