package com.stech.quiz.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;

@Configuration
public class AlertConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
            .commonTags("application", "quiz-management");
    }
}
