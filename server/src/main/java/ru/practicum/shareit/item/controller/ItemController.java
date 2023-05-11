package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;
    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public Item create(@RequestHeader(USER_ID_REQUEST_HEADER) Long userId, @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader(USER_ID_REQUEST_HEADER) Long userId,
                       @PathVariable Long itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Item get(@RequestHeader(USER_ID_REQUEST_HEADER) Long userId, @PathVariable Long itemId) {
        return itemService.get(itemId, userId);
    }

    @GetMapping
    public List<Item> getAll(@RequestHeader(USER_ID_REQUEST_HEADER) Long userId,
                             @RequestParam(required = false) Integer from,
                             @RequestParam(required = false) Integer size) {
        Pageable page = getPage(from, size);
        return itemService.getAll(userId, page);
    }

    @GetMapping("/search")
    public List<Item> findAvailable(@RequestParam(required = false) String text,
                                    @RequestParam(required = false) Integer from,
                                    @RequestParam(required = false) Integer size) {
        Pageable page = getPage(from, size);
        return itemService.findAvailable(text, page);
    }

    @PostMapping("{itemId}/comment")
    public Comment createComment(@RequestHeader(USER_ID_REQUEST_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {
        return commentService.createComment(userId, itemId, commentDto);
    }

    private Pageable getPage(Integer from, Integer size) {
        return PageRequest.of(from == null && size == null ? 0 : from / size, size == null ? Integer.MAX_VALUE : size);
    }
}
