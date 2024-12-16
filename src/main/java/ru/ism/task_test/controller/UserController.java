package ru.ism.task_test.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ism.task_test.domain.dto.in.CommentInDto;
import ru.ism.task_test.domain.dto.out.CommentDto;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.model.StatusOfTask;
import ru.ism.task_test.domain.model.User;
import ru.ism.task_test.service.TaskService;
import ru.ism.task_test.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Контроллер задач, доступен авторизованным пользователям")
@Validated
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    @PostMapping("/comment")
    @SecurityRequirement(name = "JWT")
    @Schema(description = "Добавленеие комментария к задаче исполнителем и автором")
    CommentDto addComment(@RequestBody @Valid CommentInDto commentInDto) {
        User user = userService.getCurrentUser();
        CommentDto commentDto = taskService.addComment(user.getId(), commentInDto);
        log.info("Добавлен комментарий к задаче с Id= {}", commentInDto.getTaskId());
        return commentDto;
    }

    @PatchMapping("/task")
    @SecurityRequirement(name = "JWT")
    @Schema(description = "Изменение статуса задачи исполнителем и автором")
    TaskOutDto patchStatus(@RequestParam("taskId") @Positive Long taskId,
                           @RequestParam("status") StatusOfTask status) {
        User user = userService.getCurrentUser();
        TaskOutDto task = taskService.setStatusTask(user.getId(), taskId, status);
        log.info("Статус задачи с Id= {} изменен на {}", taskId, status);
        return task;
    }

    @GetMapping("/task")
    @SecurityRequirement(name = "JWT")
    @Schema(description = "Список всех задач в которых пользователь является исполнителем")
    List<TaskOutDto> findAllByUser(@RequestParam(name = "from", required = false, defaultValue = "0")
                                   Integer from,
                                   @RequestParam(name = "size", required = false, defaultValue = "10")
                                   Integer size) {
        User user = userService.getCurrentUser();
        var list = taskService.findTask(null, user.getId(), PageRequest.of(from / size, size));
        log.info("Получен список задач для пользователя с Id= {}", user.getId());
        return list;
    }
}
