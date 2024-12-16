package ru.ism.task_test.domain.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ism.task_test.controller.AdminController;
import ru.ism.task_test.domain.dto.in.CommentInDto;
import ru.ism.task_test.domain.model.Comment;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Testcontainers
class CommentMapperTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");
    @Autowired
    private CommentMapper commentMapper;
    @MockBean
    private AdminController adminController;

    @Test
    void dtoToModels() {
        CommentInDto commentInDto = CommentInDto.builder().taskId(5L).text("test").build();
        Comment comment = commentMapper.dtoToModels(commentInDto);
        Assertions.assertEquals(comment.getTask().getId(), 5L);
    }
}