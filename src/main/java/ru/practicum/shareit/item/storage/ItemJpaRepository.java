package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserId(Long userId);
    List<Item> findByUserIdOrderById(Long userId);
    List<Item> findAllByNameIgnoreCaseContainingOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String name, String description);
    Item findByIdIs(Long itemId);
    Optional<Item> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);
}
