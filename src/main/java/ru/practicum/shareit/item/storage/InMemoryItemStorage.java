package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemRepository {
    private final HashMap<Long, Item> itemStorage = new HashMap<>();

    private Long newId = 0L;

    private long getNewId() {
        return ++newId;
    }

    @Override
    public Item create(Item item) {
        item.setId(getNewId());
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        itemStorage.remove(item.getId());
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item get(Long itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public List<Item> getAll(Long userId) {
        return itemStorage.values().stream()
                .filter(i -> i.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean remove(Long itemId) {
        itemStorage.remove(itemId);
        return true;
    }

    @Override
    public List<Item> findAvailable(String text) {
        return itemStorage.values().stream()
                .filter(item -> item.getAvailable() == true &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .collect(Collectors.toList());
    }
}