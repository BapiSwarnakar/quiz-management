package com.stech.quiz.monitoring;

import com.stech.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizSystemHealthIndicator implements HealthIndicator {
    private final QuizService quizService;

    @Override
    public Health health() {
        try {
            long activeQuizCount = quizService.getActiveQuizCount();
            return Health.up()
                    .withDetail("activeQuizzes", activeQuizCount)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withException(e)
                    .build();
        }
    }
}
