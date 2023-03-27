package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemRepository {
    private final HashMap<Long, Item> storage = new HashMap<>();

    private Long newId = 0l;

    private long getNewId() {
        return ++newId;
    }

    @Override
    public Item create(Item item) {
        item.setId(getNewId());
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        storage.remove(item.getId());
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item get(Long itemId) {
        return storage.get(itemId);
    }

    @Override
    public List<Item> getAll(Long userId) {
        return storage.values().stream()
                .filter(i -> i.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean remove(Long itemId) {
        storage.remove(itemId);
        return true;
    }

    @Override
    public List<Item> findAvailable(String text) {
        return storage.values().stream()
                .filter(item -> item.getAvailable() == true &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .collect(Collectors.toList());
    }
}