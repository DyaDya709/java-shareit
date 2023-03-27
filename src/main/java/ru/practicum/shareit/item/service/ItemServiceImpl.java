package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository ITEM_REPOSITORY;
    private final UserRepository USER_REPOSITORY;

    public ItemServiceImpl(ItemRepository ITEM_REPOSITORY, UserRepository USER_REPOSITORY) {
        this.ITEM_REPOSITORY = ITEM_REPOSITORY;
        this.USER_REPOSITORY = USER_REPOSITORY;
    }

    @Override
    public Item create(Long userId, ItemDto itemDto) {
        if (userId == null) {
            throw new BadRequestException("bad request, userId is missing");
        }
        if (!USER_REPOSITORY.isUserPresent(userId)) {
            throw new NotFoundException(String.format("user with id %s not found", userId));
        }
        User user = USER_REPOSITORY.get(userId);
        if (user == null) {
            throw new NotFoundException(String.format("user with id %s not found", userId));
        }
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new BadRequestException("bad request, name is empty");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new BadRequestException("bad request, description is empty");
        }
        if (itemDto.getAvailable() == null) {
            throw new BadRequestException("bad request, available is empty");
        }
        Item item = ITEM_REPOSITORY.create(ItemMapper.makeItem(itemDto));
        item.setOwnerId(userId);
        return item;
    }

    @Override
    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        if (userId == null) {
            throw new BadRequestException("bad request, userId is missing");
        }
        if (itemId == null) {
            throw new BadRequestException("bad request, itemId is missing");
        }
        Item item = ITEM_REPOSITORY.get(itemId);
        if (item == null) {
            throw new NotFoundException(String.format("item with id %s not found", itemId));
        }
        if (!item.getOwnerId().equals(userId)) {
            throw new NotFoundException("wrong item owner");
        }
        User user = USER_REPOSITORY.get(userId);
        if (user == null) {
            throw new NotFoundException(String.format("user with id %s not found", userId));
        }
        if (itemDto.getName() != null && !itemDto.getName().isEmpty() && !itemDto.getName().equals(item.getName())) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty() &&
                !itemDto.getDescription().equals(item.getDescription())) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null && !itemDto.getAvailable().equals(item.getAvailable())) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ITEM_REPOSITORY.update(item);
    }

    @Override
    public Item get(Long itemId) {
        return ITEM_REPOSITORY.get(itemId);
    }

    @Override
    public List<Item> getAll(Long userId) {
        return ITEM_REPOSITORY.getAll(userId);
    }

    @Override
    public Boolean remove(Long itemId) {
        return ITEM_REPOSITORY.remove(itemId);
    }

    @Override
    public List<Item> findAvailable(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return ITEM_REPOSITORY.findAvailable(text);
    }
}
