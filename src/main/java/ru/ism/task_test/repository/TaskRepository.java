package ru.ism.task_test.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ism.task_test.domain.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM tasks AS t WHERE t.id IN ( SELECT t0.id FROM tasks AS t0 " +
            "LEFT JOIN performers_users pu on t0.id = pu.task_id WHERE (pu.user_id = ?2 OR ?2 IS NULL) " +
            "AND (t0.author_id = ?1 OR ?1 IS NULL) GROUP BY t0.id) ORDER BY t.id", nativeQuery = true)
    List<Task> findBySameParam(Long authorId, Long performerId, Pageable pageable);



}
