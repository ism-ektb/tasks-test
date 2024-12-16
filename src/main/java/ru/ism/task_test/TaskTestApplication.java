package ru.ism.task_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TaskTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTestApplication.class, args);
	}

}
