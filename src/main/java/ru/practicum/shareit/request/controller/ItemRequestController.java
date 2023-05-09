package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private static final String requestHeaderUserId = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDto create(@RequestHeader(requestHeaderUserId) long userId,
                                 @Valid @RequestBody ItemRequestDto request) {
        request.setUserId(userId);
        return itemRequestService.create(request);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequests(@RequestHeader(requestHeaderUserId) long userId) {
        return itemRequestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherRequests(@RequestHeader(requestHeaderUserId) long userId,
                                                 @RequestParam(required = false) Integer from,
                                                 @RequestParam(required = false) Integer size) {
        if (from == null || size == null) {
            return Collections.emptyList();
        } else if (from < 0 || size <= 0) {
            throw new BadRequestException("Invalid page request");
        }
        Pageable page = PageRequest.of(from / size, size);
        return itemRequestService.getOtherRequests(userId, page);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(requestHeaderUserId) long userId,
                                         @PathVariable Long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
