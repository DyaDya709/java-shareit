package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service

public class ItemServiceImpl implements ItemService {
    @Override
    public ItemDto create(Long userId,ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto update(Long userId,Long itemId,ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto get(Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return null;
    }

    @Override
    public Boolean remove(Long id) {
        return null;
    }

    @Override
    public List<ItemDto> findAvailable(String text) {
        return null;
    }
}
