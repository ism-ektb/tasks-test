package ru.ism.task_test.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ism.task_test.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable("users_email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User AS u " +
            "WHERE u.id IN ?1")
    List<User> findById(List<Long> ids);
}
