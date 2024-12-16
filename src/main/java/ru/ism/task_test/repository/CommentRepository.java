package ru.ism.task_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ism.task_test.domain.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
