package ru.ism.task_test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ism.task_test.domain.dto.in.TaskNewDto;
import ru.ism.task_test.domain.dto.in.TaskPatchAdminDto;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.dto.out.UserDto;
import ru.ism.task_test.domain.model.User;
import ru.ism.task_test.service.TaskService;
import ru.ism.task_test.service.UserService;

import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Контроллер администратора")
public class AdminController {

    private final TaskService service;
    private final UserService userService;

    @PostMapping("/task")
    @Operation(summary = "Создание задачи администратором")
    @SecurityRequirement(name = "JWT")
    TaskOutDto create(@RequestBody @Valid TaskNewDto taskNewDto) {
        User user = userService.getCurrentUser();
        TaskOutDto taskOutDto = service.createTask(taskNewDto, user);
        log.info("Создана новая задача title= {} ID= {}", taskOutDto.getTitle(), taskOutDto.getId());
        return taskOutDto;
    }

    @PatchMapping("/task/{taskId}")
    @Operation(summary = "редактирование задачи администратором")
    @SecurityRequirement(name = "JWT")
    TaskOutDto patch(@PathVariable("taskId") @Positive Long taskId,
                     @RequestBody @Valid TaskPatchAdminDto taskPatchAdminDto) {
        TaskOutDto taskOutDto = service.patchTask(taskId, taskPatchAdminDto);
        log.info("Задача с Id= {} изменена", taskId);
        return taskOutDto;
    }

    @GetMapping("/task")
    @Operation(summary = "Поиск задач по автору и исполнителю")
    @SecurityRequirement(name = "JWT")
    List<TaskOutDto> findTask(@RequestParam(name = "authorId", required = false) Long authorId,
                              @RequestParam(name = "performerId", required = false) Long performerId,
                              @RequestParam(name = "from", required = false, defaultValue = "0")
                              Integer from,
                              @RequestParam(name = "size", required = false, defaultValue = "10")
                              Integer size) {
        var dto = service.findTask(authorId, performerId, PageRequest.of(from / size, size));
        log.info("проведен поиск задач по параметрам");
        return dto;
    }


    @GetMapping("/users")
    @Operation(summary = "Получение администратором списка всех пользователей")
    @SecurityRequirement(name = "JWT")
    public List<UserDto> getAtt() {
        List<UserDto> list = service.getAll();
        log.info("Получен список всех пользователей");
        return list;
    }
}
