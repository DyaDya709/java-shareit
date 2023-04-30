package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final String requestHeaderUserId = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDto create(@RequestHeader(requestHeaderUserId) long userId,
                                 @Valid @RequestBody ItemRequestDto request) {
        request.setUserId(userId);
        return itemRequestService.create(request);
    }
}
