package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.service.CommentService;
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
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;
    private final String userIdRequestHeader = "X-Sharer-User-Id";

    @PostMapping
    public Item create(@RequestHeader(userIdRequestHeader) Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader(userIdRequestHeader) Long userId,
                       @PathVariable Long itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Item get(@RequestHeader(userIdRequestHeader) Long userId, @PathVariable Long itemId) {
        return itemService.get(itemId, userId);
    }

    @GetMapping
    public List<Item> getAll(@RequestHeader(userIdRequestHeader) Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<Item> findAvalible(@RequestParam(required = false) String text) {
        return itemService.findAvailable(text);
    }

    @PostMapping("{itemId}/comment")
    public Comment createComment(@RequestHeader(userIdRequestHeader) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return commentService.createComment(userId, itemId, commentDto);
    }
}
