package ru.ism.task_test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ism.task_test.domain.dto.in.CommentInDto;
import ru.ism.task_test.domain.dto.in.TaskNewDto;
import ru.ism.task_test.domain.dto.in.TaskPatchAdminDto;
import ru.ism.task_test.domain.dto.out.CommentDto;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.dto.out.UserDto;
import ru.ism.task_test.domain.mapper.*;
import ru.ism.task_test.domain.model.Comment;
import ru.ism.task_test.domain.model.StatusOfTask;
import ru.ism.task_test.domain.model.Task;
import ru.ism.task_test.domain.model.User;
import ru.ism.task_test.exception.exception.BaseRelationshipException;
import ru.ism.task_test.exception.exception.NoFoundObjectException;
import ru.ism.task_test.repository.CommentRepository;
import ru.ism.task_test.repository.TaskRepository;
import ru.ism.task_test.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final TaskListMapper listMapper;
    private final UserRepository userRepository;
    private final UserListMapper userListMapper;
    private final TaskPatcher patcher;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    /**
     * Получение списка всех пользователй
     *
     * @return
     */
    @Override
    public List<UserDto> getAll() {
        return userListMapper.modelsToDto(userRepository.findAll());
    }

    /**
     * Создание новой задачи администратором
     *
     * @param taskNewDto
     * @param user
     * @return
     */
    @Override
    public TaskOutDto createTask(TaskNewDto taskNewDto, User user) {

        Task task = mapper.dtoToModels(taskNewDto);
        task.setAuthor(user);
        Task newTask = repository.save(task);
        return mapper.modelToDto(newTask);
    }

    /**
     * Редактирование задачи администратором
     *
     * @param taskId
     * @param patchAdminDto
     * @return
     */
    @Override
    public TaskOutDto patchTask(Long taskId, TaskPatchAdminDto patchAdminDto) {
        Task task = repository.findById(taskId).orElseThrow(() -> new NoFoundObjectException(String.format(
                "Задача с Id= %s отсутствует", taskId)));
        Task patch = patcher.patch(task, patchAdminDto);

        return mapper.modelToDto(repository.save(patch));
    }

    /**
     * Поиск задач по автору и исполнителю
     *
     * @param authorId
     * @param performerId
     * @param pageable
     * @return
     */
    @Override
    public List<TaskOutDto> findTask(Long authorId, Long performerId, Pageable pageable) {

        return listMapper.modelsToDtos(repository.findBySameParam(authorId, performerId, pageable));
    }

    /**
     * Смена статуса задачи, доступна только автору и исполнителю задачи
     *
     * @param userId
     * @param taskId
     * @param status
     * @return
     */
    @Override
    public TaskOutDto setStatusTask(Long userId, Long taskId, StatusOfTask status) {
        Task task = repository.findById(taskId).orElseThrow(()
                -> new NoFoundObjectException(String.format("Задача с Id= %s не сушествует", taskId)));
        List<Long> performers = task.getPerformers().stream().map(a -> a.getId()).toList();
        if (!(task.getAuthor().getId().equals(userId)) && !(performers.contains(userId)))
            throw new BaseRelationshipException(
                    String.format("Менять статус задачи с Id= %s может только автор или исполнитель", taskId));
        task.setStatus(status);
        Task newTask = repository.save(task);
        return mapper.modelToDto(newTask);
    }

    /**
     * Добавление комментария к задаче. Доступна только для автора и исполнителя задачи
     *
     * @param userId
     * @param commentInDto
     * @return
     */
    @Override
    public CommentDto addComment(Long userId, CommentInDto commentInDto) {
        Task task = repository.findById(commentInDto.getTaskId()).orElseThrow(()
                -> new NoFoundObjectException(String.format("Задача с Id= %s не сушествует",
                commentInDto.getTaskId())));
        List<Long> performers = task.getPerformers().stream().map(a -> a.getId()).toList();
        if (!(task.getAuthor().getId().equals(userId)) && !(performers.contains(userId)))
            throw new BaseRelationshipException(String.format("Менять статус задачи с Id= %s " +
                    "может только автор или исполнитель", commentInDto.getTaskId()));
        Comment comment = commentMapper.dtoToModels(commentInDto);
        comment.setAuthor(User.builder().id(userId).build());
        return commentMapper.modelsToDto(commentRepository.save(comment));
    }
}
