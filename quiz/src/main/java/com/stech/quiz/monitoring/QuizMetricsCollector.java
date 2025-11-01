package com.stech.quiz.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QuizMetricsCollector {
    private final MeterRegistry registry;
    private final Counter quizStartCounter;
    private final Counter quizCompletionCounter;

    public QuizMetricsCollector(MeterRegistry registry) {
        this.registry = registry;
        this.quizStartCounter = Counter.builder("quiz.starts")
            .description("Number of quizzes started")
            .register(registry);
        this.quizCompletionCounter = Counter.builder("quiz.completions")
            .description("Number of quizzes completed")
            .register(registry);
    }

    public void incrementQuizStarted() {
        quizStartCounter.increment();
    }

    public void incrementQuizCompleted() {
        quizCompletionCounter.increment();
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("quizStarts", quizStartCounter.count());
        metrics.put("quizCompletions", quizCompletionCounter.count());
        metrics.put("completionRate", calculateCompletionRate());
        return metrics;
    }

    private double calculateCompletionRate() {
        double starts = quizStartCounter.count();
        double completions = quizCompletionCounter.count();
        return starts > 0 ? (completions / starts) * 100 : 0.0;
    }
}
