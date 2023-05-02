package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.response.model.ItemResponseDto;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequestDto {
    @EqualsAndHashCode.Include
    private Long id;

    private Long userId;

    @NotEmpty
    private String description;

    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Instant created;
    @JsonProperty(value = "items")
    private Set<ItemResponseDto> itemResponseDtos;

}
