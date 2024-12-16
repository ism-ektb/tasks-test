package ru.ism.task_test.service;

import org.springframework.data.domain.Pageable;
import ru.ism.task_test.domain.dto.in.CommentInDto;
import ru.ism.task_test.domain.dto.in.TaskNewDto;
import ru.ism.task_test.domain.dto.in.TaskPatchAdminDto;
import ru.ism.task_test.domain.dto.out.CommentDto;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.dto.out.UserDto;
import ru.ism.task_test.domain.model.StatusOfTask;
import ru.ism.task_test.domain.model.User;

import java.util.List;

public interface TaskService {
    /**
     * Получение списка всех пользователй
     *
     * @return
     */
    List<UserDto> getAll();

    /**
     * Создание новой задачи администратором
     *
     * @param taskNewDto
     * @return
     */
    TaskOutDto createTask(TaskNewDto taskNewDto, User user);

    /**
     * Редактирование задачи администратором
     *
     * @param patchAdminDto
     * @return
     */
    TaskOutDto patchTask(Long taskId, TaskPatchAdminDto patchAdminDto);

    /**
     * Поиск задач по автору и исполнителю
     *
     * @param authorId
     * @param performerId
     * @param pageable
     * @return
     */
    List<TaskOutDto> findTask(Long authorId, Long performerId, Pageable pageable);

    /**
     * Смена статуса задачи, доступна только автору и исполнителю задачи
     * @param userId
     * @param taskId
     * @param status
     * @return
     */
    TaskOutDto setStatusTask(Long userId, Long taskId, StatusOfTask status);

    /**
     * Добавление комментария к задаче. Доступна только для автора и исполнителя задачи
     * @param userId
     * @param commentInDto
     * @return
     */
    CommentDto addComment(Long userId, CommentInDto commentInDto);
}
