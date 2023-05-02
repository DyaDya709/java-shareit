package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long userId, ItemDto itemDto);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    Item get(Long itemId, Long userId);

    List<Item> getAll(Long userId, Pageable page);

    Boolean remove(Long id);

    List<Item> findAvailable(String text, Pageable page);
}
