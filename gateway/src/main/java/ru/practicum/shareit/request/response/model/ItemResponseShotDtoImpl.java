package ru.practicum.shareit.request.response.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemResponseShotDtoImpl implements ItemResponseShotDto {
    private Long id;
    private Long itemId;
    private Long requestId;
}
