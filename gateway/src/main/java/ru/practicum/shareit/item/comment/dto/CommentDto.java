package ru.practicum.shareit.item.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    @NotEmpty
    private String text;

    private String author;

    private LocalDateTime created;
}
