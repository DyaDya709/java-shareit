package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Query("SELECT u FROM User u JOIN FETCH u.bookings b WHERE u.id = :userId")
    Optional<User> findByIdWithBookings(@Param("userId") Long userId);
}
