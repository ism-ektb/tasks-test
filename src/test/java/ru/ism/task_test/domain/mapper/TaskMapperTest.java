package ru.ism.task_test.domain.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ism.task_test.domain.dto.in.TaskNewDto;
import ru.ism.task_test.domain.dto.in.TaskPatchAdminDto;
import ru.ism.task_test.domain.model.Task;
import ru.ism.task_test.domain.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест проверяет логигу мапинга задачи
 */
@SpringBootTest
@Testcontainers
class TaskMapperTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private TaskMapper mapper;
    @Autowired
    private TaskPatcher patcher;

    @Test
    void dtoToModels() {
        var dto = TaskNewDto.builder()
                .title("test")
                .performersId(List.of(1L, 2L)).build();
        Task task = Task.builder().title("test")
                .performers(List.of(User.builder().id(1L).build(), User.builder().id(2L).build())).build();
        Task newTask = mapper.dtoToModels(dto);
        System.out.println(newTask);
        Assertions.assertEquals(task, newTask);

    }

    @Test
    void patcher() {
        Task task = patcher.patch(Task.builder().build(),
                TaskPatchAdminDto.builder().performersId(List.of(1L, 2L)).build());
        List<Long> list = task.getPerformers().stream().map(a -> a.getId()).toList();
        Assertions.assertEquals(list, List.of(1L, 2L));


    }
}