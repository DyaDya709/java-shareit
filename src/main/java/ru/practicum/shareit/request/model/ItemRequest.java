package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.practicum.shareit.request.response.model.ItemResponse;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Data
@Table(name = "item_requests")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Instant created;

    @OneToMany(mappedBy = "itemRequest")
    @ToString.Exclude
    @JsonManagedReference
    private Set<ItemResponse> responses;

}
