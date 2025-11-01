package com.stech.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.stech.quiz.entity")
@EnableJpaRepositories("com.stech.quiz.repository")
@EnableScheduling
public class QuizManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizManagementApplication.class, args);
	}

}
