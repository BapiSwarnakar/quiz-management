package com.stech.quiz.monitoring;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PerformanceMonitor {
    private final MeterRegistry registry;
    private final Timer quizCompletionTimer;

    public PerformanceMonitor(MeterRegistry registry) {
        this.registry = registry;
        this.quizCompletionTimer = Timer.builder("quiz.completion.time")
            .description("Time taken to complete quizzes")
            .register(registry);
    }

    public Timer.Sample startTimer() {
        return Timer.start(registry);
    }

    public void stopTimer(Timer.Sample sample) {
        sample.stop(quizCompletionTimer);
    }
}
