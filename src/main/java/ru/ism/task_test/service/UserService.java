package ru.ism.task_test.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ism.task_test.domain.model.User;

public interface UserService extends UserDetailsService {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User getCurrentUser();

    UserDetailsService userDetailsService();


}
