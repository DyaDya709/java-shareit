package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable("itemId") Long itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Item get(@PathVariable("itemId") Long itemId) {
        return itemService.get(itemId);
    }

    @GetMapping
    public List<Item> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<Item> findAvalible(@RequestParam(required = false) String text) {
        return itemService.findAvailable(text);
    }
}
