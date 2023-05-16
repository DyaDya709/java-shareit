package ru.practicum.shareit.item.comment.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Getter
@Setter
public class Comment {
    private Long id;
    private String text;
    private LocalDateTime created;
    private String authorName;
    private Long userId;
    private Item item;
}
