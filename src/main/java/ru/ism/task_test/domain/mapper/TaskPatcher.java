package ru.ism.task_test.domain.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ism.task_test.domain.dto.in.TaskPatchAdminDto;
import ru.ism.task_test.domain.model.Task;
import ru.ism.task_test.domain.model.User;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskPatcher {

    public Task patch(Task task, TaskPatchAdminDto patch) {
        if (patch == null || task == null) return null;
        Task newTask = Task.builder()
                .id(task.getId())
                .title(patch.getTitle() != null ? patch.getTitle() : task.getTitle())
                .description(patch.getDescription() != null ? patch.getDescription() : task.getDescription())
                .status(patch.getStatus() != null ? patch.getStatus() : task.getStatus())
                .priority(patch.getPriority() != null ? patch.getPriority() : task.getPriority())
                .author(task.getAuthor())
                .comments(task.getComments())
                .performers(patch.getPerformersId() != null ?
                        patch.getPerformersId().stream().map(a -> User.builder().id(a).build()).collect(Collectors.toList())
                        : task.getPerformers())
                .build();

        return newTask;
    }
}
