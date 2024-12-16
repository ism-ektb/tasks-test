package ru.ism.task_test.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.ism.task_test.filter.JwtFilter;
import ru.ism.task_test.service.UserService;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Конфигурация безопасности
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFIlter;

    private final UserService userService;
    private static final String[] SWAGGER_PATHS = {"/swagger-ui.html", "/v3/api-docs/**",
            "/swagger-ui/**", "/webjars/swagger-ui/**"};


    /**
     * Этот метод создает и настраивает цепочку фильтров безопасности для HTTP.
     *
     * @param http объект HttpSecurity для настройки цепочки фильтров безопасности
     * @return созданная цепочка фильтров безопасности
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable); // Отключаем защиту от CSRF

        // Настраиваем авторизацию запросов
        http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login/**", "/registration/**", "/css/**", "/refresh_token/**", "/")
                            .permitAll();
                    auth.requestMatchers(SWAGGER_PATHS).permitAll();// Разрешаем все запросы к этим URL
                    auth.requestMatchers("/admin/**").hasRole("ADMIN"); // Разрешаем запросы только для администратора
                    auth.anyRequest().authenticated(); // Требуем аутентификацию для всех остальных запросов
                })

                .userDetailsService(userService)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // Управление сессиями
                .addFilterBefore(jwtFIlter, UsernamePasswordAuthenticationFilter.class); // Добавление фильтра JWT перед фильтром UsernamePasswordAuthenticationFilter
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
