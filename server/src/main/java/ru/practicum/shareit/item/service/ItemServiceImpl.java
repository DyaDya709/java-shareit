package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.storage.BookingJpaRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.request.response.service.ItemResponseService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserJpaRepository userJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final BookingJpaRepository bookingJpaRepository;
    private final ItemResponseService itemResponseService;

    @Override
    @Transactional
    public Item create(Long userId, ItemDto itemDto) {
        if (userId == null) {
            throw new BadRequestException("bad request, userId is missing");
        }
        User user = userJpaRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NotFoundException(String.format("user with id %s not found", userId));
        }

        Item item;
        //Если есть requestId, значит это ответ на запрос вещи
        if (itemDto.getRequestId() != null) {
            //Проверим, есть такая вещь у пользователя
            item = itemJpaRepository.findByUserIdAndNameContainingIgnoreCase(userId, itemDto.getName())
                    .orElse(null);
            //если это новая вещь, то создадим ее
            if (item == null) {
                item = ItemMapper.toEntity(itemDto);
                item.setUserId(userId);
                item = itemJpaRepository.save(item);
            }
            itemResponseService.create(itemDto.getRequestId(), item);
            item.setRequestId(itemDto.getRequestId());
        } else {
            item = ItemMapper.toEntity(itemDto);
            item.setUserId(userId);
        }
        return itemJpaRepository.save(item);
    }

    @Override
    @Transactional
    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        if (userId == null) {
            throw new BadRequestException("bad request, userId is missing");
        }
        if (itemId == null) {
            throw new BadRequestException("bad request, itemId is missing");
        }
        Item item = itemJpaRepository.findById(itemId).orElse(null);
        ;
        if (item == null) {
            throw new NotFoundException(String.format("item with id %s not found", itemId));
        }

        if (!item.getUserId().equals(userId)) {
            throw new NotFoundException("wrong item owner");
        }
        User user = userJpaRepository.findById(userId).orElse(null);
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
        return itemJpaRepository.save(item);
    }

    @Override
    public Item get(Long itemId, Long userId) {
        Item item = itemJpaRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("item with id %s not found", itemId)));
        if (item.getUserId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            item.setNextBooking(bookingJpaRepository.findNextBookingByItemIds(item.getId(), now).orElse(null));
            item.setLastBooking(bookingJpaRepository.findLastBookingByItemIds(item.getId(), now).orElse(null));
        }

        return item;
    }

    @Override
    public List<Item> getAll(Long userId, Pageable page) {
        List<Item> items = itemJpaRepository.findByUserIdOrderById(userId);
        LocalDateTime now = LocalDateTime.now();
        for (Item item : items) {
            if (!item.getUserId().equals(userId)) {
                continue;
            }
            item.setNextBooking(bookingJpaRepository.findNextBookingByItemIds(item.getId(), now).orElse(null));
            item.setLastBooking(bookingJpaRepository.findLastBookingByItemIds(item.getId(), now).orElse(null));
        }
        return items;
    }

    @Override
    @Transactional
    public Boolean remove(Long itemId) {
        itemJpaRepository.deleteById(itemId);
        return true;
    }

    @Override
    public List<Item> findAvailable(String text, Pageable page) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemJpaRepository
                .findAllByNameIgnoreCaseContainingOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text);
    }
}
