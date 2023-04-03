package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);

    Item update(Item item);

    Item get(Long itemId);

    List<Item> getAll(Long userId);

    Boolean remove(Long itemId);

    List<Item> findAvailable(String text);
}
