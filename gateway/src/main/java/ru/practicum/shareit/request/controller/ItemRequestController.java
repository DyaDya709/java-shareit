package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.Collections;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private static final String requestHeaderUserId = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader(requestHeaderUserId) long userId,
                                         @Valid @RequestBody ItemRequestDto request) {
        request.setUserId(userId);
        return itemRequestClient.create(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequests(@RequestHeader(requestHeaderUserId) long userId) {
        return itemRequestClient.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherRequests(@RequestHeader(requestHeaderUserId) long userId,
                                                   @RequestParam(required = false) Integer from,
                                                   @RequestParam(required = false) Integer size) {
        if (from == null || size == null) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else if (from < 0 || size <= 0) {
            throw new BadRequestException("Invalid page request");
        }
        return itemRequestClient.getOtherRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(requestHeaderUserId) long userId,
                                                 @PathVariable Long requestId) {
        return itemRequestClient.getRequestById(requestId, userId);
    }
}
