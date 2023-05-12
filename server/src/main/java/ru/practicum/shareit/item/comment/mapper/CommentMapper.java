package ru.practicum.shareit.item.comment.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(comment.getAuthorName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public static Comment toEntity(CommentDto commentDto, User user, Item item) {
        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setText(commentDto.getText());
        comment.setAuthorName(user.getName());
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        return comment;
    }
}
