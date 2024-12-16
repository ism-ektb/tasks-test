package ru.ism.task_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ism.task_test.domain.dto.in.LoginRequestDto;
import ru.ism.task_test.domain.dto.in.RegistrationRequestDto;
import ru.ism.task_test.domain.dto.in.TaskNewDto;
import ru.ism.task_test.domain.dto.out.AuthenticationResponseDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.ism.task_test.domain.model.PriorityOfTask.LOW;


@SpringBootTest
@Testcontainers
class IntegrationControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    /**
     * Регистрация с уже существуюющим емайл
     */

    @Test
    @SneakyThrows
    void registerFail_emailIsNotUniq() {
        RegistrationRequestDto request = RegistrationRequestDto.builder()
                .email("admin@admin.ru")
                .password("password").build();
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(409));

    }

    @Test
    @SneakyThrows
    void testLogin_andGoodRequest() {
        var request = LoginRequestDto.builder()
                .email("admin@admin.ru")
                .password("password").build();
        String response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(request))).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        var dto = mapper.readValue(response, AuthenticationResponseDto.class);

        mockMvc.perform(get("/admin/users")
                        .header("Authorization", "Bearer " + dto.getAccessToken()))
                .andExpect(status().isOk());
    }

    /**
     * регистрация с невалидным email
     */
    @Test
    @SneakyThrows
    void registerFail_emailIsNotValid() {
        RegistrationRequestDto request = RegistrationRequestDto.builder()
                .email("adminadmin.ru")
                .password("password").build();
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(400));

    }

    @Test
    @SneakyThrows
    void createNewTask_goodResult() {

        var request = LoginRequestDto.builder()
                .email("admin@admin.ru")
                .password("password").build();
        String response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        var dto = mapper.readValue(response, AuthenticationResponseDto.class);

        TaskNewDto taskNewDto = TaskNewDto.builder().title("test").performersId(List.of(1L)).priority(LOW).build();
        mockMvc.perform(post("/admin/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(taskNewDto))
                        .header("Authorization", "Bearer " + dto.getAccessToken()))
                .andExpect(status().isOk());

    }

    /**
     * Указан Id несуществующего исполнителя
     */
    @Test
    @SneakyThrows
    void createNewTask_badPerformersId() {

        var request = LoginRequestDto.builder()
                .email("admin@admin.ru")
                .password("password").build();
        String response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        var dto = mapper.readValue(response, AuthenticationResponseDto.class);
        //Пользователь 2 отсутствует в базе. Исполнителем быть не может
        TaskNewDto taskNewDto = TaskNewDto.builder().title("test").performersId(List.of(1L, 2L)).priority(LOW).build();
        mockMvc.perform(post("/admin/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(taskNewDto))
                        .header("Authorization", "Bearer " + dto.getAccessToken()))
                .andExpect(status().is(409));

    }

}