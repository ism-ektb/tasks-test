package ru.ism.task_test.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ism.task_test.domain.model.Task;

@Mapper(componentModel = "spring")
public interface TaskLongMapper {
    @Mapping(target = "id", source = "taskId")
    Task longToModel(Long taskId);
}
