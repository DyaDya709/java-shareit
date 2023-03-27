package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemRepository {
    private final HashMap<Long, Item> ITEM_STORAGE = new HashMap<>();

    private Long newId = 0L;

    private long getNewId() {
        return ++newId;
    }

    @Override
    public Item create(Item item) {
        item.setId(getNewId());
        ITEM_STORAGE.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        ITEM_STORAGE.remove(item.getId());
        ITEM_STORAGE.put(item.getId(), item);
        return item;
    }

    @Override
    public Item get(Long itemId) {
        return ITEM_STORAGE.get(itemId);
    }

    @Override
    public List<Item> getAll(Long userId) {
        return ITEM_STORAGE.values().stream()
                .filter(i -> i.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean remove(Long itemId) {
        ITEM_STORAGE.remove(itemId);
        return true;
    }

    @Override
    public List<Item> findAvailable(String text) {
        return ITEM_STORAGE.values().stream()
                .filter(item -> item.getAvailable() == true &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .collect(Collectors.toList());
    }
}