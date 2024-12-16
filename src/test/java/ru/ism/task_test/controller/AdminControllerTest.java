package ru.ism.task_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.ism.task_test.config.SecurityConfig;
import ru.ism.task_test.domain.dto.in.TaskNewDto;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.model.PriorityOfTask;
import ru.ism.task_test.service.JwtService;
import ru.ism.task_test.service.TaskService;
import ru.ism.task_test.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerTest {
    @MockBean
    private TaskService service;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUser(roles = "ADMIN")
    @SneakyThrows
    void adminGetListUser_good() {
        mvc.perform(get("/admin/users")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @SneakyThrows
    void userGetListUser_false() {
        mvc.perform(get("/admin/users")).andExpect(status().is(403));
    }


    @Test
    @SneakyThrows
    void findAllUsers() {
        mvc.perform(get("/admin/task")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void createTask() {
        TaskNewDto dto = TaskNewDto.builder()
                .title("Task")
                .performersId(List.of(1L, 2L))
                .priority(PriorityOfTask.LOW).build();
        System.out.println(mapper.writeValueAsString(dto));
        when(service.createTask(any(), any())).thenReturn(TaskOutDto.builder().build());
        mvc.perform(post("/admin/task")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto))).andExpect(status().isOk());
    }

}