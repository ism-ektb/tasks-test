package ru.ism.task_test.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ism.task_test.domain.dto.in.CommentInDto;
import ru.ism.task_test.domain.dto.in.TaskNewDto;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.model.PriorityOfTask;
import ru.ism.task_test.domain.model.StatusOfTask;
import ru.ism.task_test.domain.model.Task;
import ru.ism.task_test.domain.model.User;
import ru.ism.task_test.exception.exception.BaseRelationshipException;
import ru.ism.task_test.repository.TaskRepository;
import ru.ism.task_test.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест проверяет логику поиска задачи по Id автора и Id исполнителя
 */
@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceImplTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");
    @Autowired
    private TaskService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @Order(1)
    void findAllTask() {
        // заполняем таблицу данными которые используем в следующих тестах
        userRepository.save(User.builder().email("1@1.1").password("pass").build());
        userRepository.save(User.builder().email("2@1.1").password("pass").build());
        service.createTask(TaskNewDto.builder().title("test").priority(PriorityOfTask.LOW)
                .performersId(List.of(2L, 3L)).build(), User.builder().id(1L).build());
        service.createTask(TaskNewDto.builder().title("test2").priority(PriorityOfTask.LOW)
                .performersId(List.of(1L, 3L)).build(), User.builder().id(1L).build());
        service.createTask(TaskNewDto.builder().title("test3").priority(PriorityOfTask.LOW)
                .performersId(List.of(1L, 2L, 3L)).build(), User.builder().id(1L).build());

        List<TaskOutDto> list = service.findTask(null, null, null);
        List<Long> longList = list.stream().map(a -> a.getId()).toList();
        assertEquals(longList, List.of(1L, 2L, 3L));
    }

    @Test
    void findTaskWithPerformers_3() {
        List<TaskOutDto> list = service.findTask(null, 3L, null);
        List<Long> longList = list.stream().map(a -> a.getId()).toList();
        assertEquals(longList, List.of(1L, 2L, 3L));
    }

    @Test
    void findTaskWithPerformers_2() {
        List<TaskOutDto> list = service.findTask(null, 2L, null);
        List<Long> longList = list.stream().map(a -> a.getId()).toList();
        assertEquals(longList, List.of(1L, 3L));
    }

    @Test
    void findTaskWithAuthor_3() {
        List<TaskOutDto> list = service.findTask(3L, 3L, null);
        assertEquals(list, List.of());
    }

    @Test
    void addComment() {
        service.addComment(3L, CommentInDto.builder().taskId(2L).text("test").build());
        Task task = taskRepository.findById(2L).get();
        assertEquals(task.getComments().size(), 1);
    }

    /**
     * Комментатор не является ни автором ни исполнителем задачи
     */
    @Test
    void addComment_badUser_false() {
        assertThrows(BaseRelationshipException.class,
                () -> service.addComment(2L, CommentInDto.builder().taskId(2L).text("test").build()));
    }

    @Test
    void setStatus() {
        TaskOutDto task = service.setStatusTask(3L, 2L, StatusOfTask.COMPLETED);
        assertEquals(task.getStatus(), StatusOfTask.COMPLETED);
    }

    /**
     * пользователь не являктся ни автором ни исполнителем задачи
     */
    @Test
    void setStatus_badUser_false() {
        assertThrows(BaseRelationshipException.class,
                () -> service.setStatusTask(2L, 2L, StatusOfTask.COMPLETED));
    }

}