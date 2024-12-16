package ru.ism.task_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.ism.task_test.config.SecurityConfig;
import ru.ism.task_test.domain.dto.in.CommentInDto;
import ru.ism.task_test.domain.dto.out.CommentDto;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.model.User;
import ru.ism.task_test.service.JwtService;
import ru.ism.task_test.service.TaskService;
import ru.ism.task_test.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtService jwtService;


    @Test
    @WithMockUser
    @SneakyThrows
    void addComment_goodResult() {
        CommentInDto comment = CommentInDto.builder().taskId(1L).text("text").build();
        when(taskService.addComment(anyLong(), any())).thenReturn(CommentDto.builder().build());
        when(userService.getCurrentUser()).thenReturn(User.builder().build());
        mvc.perform(post("/user/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(comment))).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    void addComment_anonymousUser_fail() {
        CommentInDto comment = CommentInDto.builder().taskId(1L).text("text").build();
        when(taskService.addComment(anyLong(), any())).thenReturn(CommentDto.builder().build());
        when(userService.getCurrentUser()).thenReturn(User.builder().build());
        mvc.perform(post("/user/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(comment))).andExpect(status().is(403));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void addComment_noTextOfComment_fail() {
        CommentInDto comment = CommentInDto.builder().taskId(1L).build();
        when(taskService.addComment(anyLong(), any())).thenReturn(CommentDto.builder().build());
        when(userService.getCurrentUser()).thenReturn(User.builder().build());
        mvc.perform(post("/user/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(comment))).andExpect(status().is(400));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void patchStatus() {
        when(taskService.setStatusTask(anyLong(), anyLong(), any()))
                .thenReturn(TaskOutDto.builder().build());
        when(userService.getCurrentUser()).thenReturn(User.builder().build());
        mvc.perform(patch("/user/task")
                .param("taskId", "1")
                .param("status", "IN_PROGRESS")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void patchStatus_falseStatus() {
        when(taskService.setStatusTask(anyLong(), anyLong(), any()))
                .thenReturn(TaskOutDto.builder().build());
        when(userService.getCurrentUser()).thenReturn(User.builder().build());
        mvc.perform(patch("/user/task")
                .param("taskId", "1")
                .param("status", "IN_PROGRESS_00000000")).andExpect(status().is(400));
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    void patchStatus_Anonymous() {
        when(taskService.setStatusTask(anyLong(), anyLong(), any()))
                .thenReturn(TaskOutDto.builder().build());
        when(userService.getCurrentUser()).thenReturn(User.builder().build());
        mvc.perform(patch("/user/task")
                .param("taskId", "1")
                .param("status", "IN_PROGRESS")).andExpect(status().is(403));
    }
}