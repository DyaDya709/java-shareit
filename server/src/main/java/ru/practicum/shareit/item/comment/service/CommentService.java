package ru.practicum.shareit.item.comment.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;

public interface CommentService {
    Comment createComment(Long userId, Long itemId, CommentDto commentDto);
}
