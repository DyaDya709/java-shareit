package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public Item create(Long userId, ItemDto itemDto) {
        if (userId == null) {
            throw new BadRequestException("bad request, userId is missing");
        }
        if (!userRepository.isUserPresent(userId)) {
            throw new NotFoundException(String.format("user with id %s not found", userId));
        }
        User user = userRepository.get(userId);
        if (user == null) {
            throw new NotFoundException(String.format("user with id %s not found", userId));
        }
        Item item = itemRepository.create(itemMapper.toEntity(itemDto));
        item.setUser(userRepository.get(userId));
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
        Item item = itemRepository.get(itemId);
        if (item == null) {
            throw new NotFoundException(String.format("item with id %s not found", itemId));
        }
        if (!item.getUser().getId().equals(userId)) {
            throw new NotFoundException("wrong item owner");
        }
        User user = userRepository.get(userId);
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
        return itemRepository.update(item);
    }

    @Override
    public Item get(Long itemId) {
        return itemRepository.get(itemId);
    }

    @Override
    public List<Item> getAll(Long userId) {
        return itemRepository.getAll(userId);
    }

    @Override
    public Boolean remove(Long itemId) {
        return itemRepository.remove(itemId);
    }

    @Override
    public List<Item> findAvailable(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findAvailable(text);
    }
}
