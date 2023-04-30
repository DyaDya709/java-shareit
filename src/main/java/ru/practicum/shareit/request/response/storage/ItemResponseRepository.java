package ru.practicum.shareit.request.response.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.response.model.ItemResponse;

public interface ItemResponseRepository extends JpaRepository<ItemResponse, Long> {

}
