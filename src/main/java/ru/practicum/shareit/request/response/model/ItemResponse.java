package ru.practicum.shareit.request.response.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;

@Data
@Entity
@Table(name = "item_responses")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_id", updatable = false, insertable = false)
    @ToString.Exclude
    private Item item;

    @ManyToOne
    @JoinColumn(name = "request_id", updatable = false, insertable = false)
    @ToString.Exclude
    private ItemRequest itemRequest;

}
