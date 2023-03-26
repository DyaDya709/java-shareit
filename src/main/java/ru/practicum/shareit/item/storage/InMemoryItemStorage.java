package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;

public class InMemoryItemStorage implements ItemRepository{
    private final HashMap<Long, Item> storage = new HashMap<>();

    private long getId() {
        long lastId = storage.keySet().stream()
                .max(Long::compareTo)
                .orElse(0l);
        return lastId + 1;
    }

    @Override
    public Item create(Item item) {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public List<Item> get() {
        return null;
    }

    @Override
    public Item getAll(Long id) {
        return null;
    }

    @Override
    public Boolean remove(Long id) {
        return null;
    }
}