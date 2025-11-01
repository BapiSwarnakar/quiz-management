package com.stech.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PerformanceMonitorService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void sendPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("heapUsage", memoryBean.getHeapMemoryUsage().getUsed());
        metrics.put("heapMax", memoryBean.getHeapMemoryUsage().getMax());
        metrics.put("activeUsers", getActiveUserCount());
        metrics.put("activeQuizzes", getActiveQuizCount());

        messagingTemplate.convertAndSend("/topic/performance", metrics);
    }

    private long getActiveUserCount() {
        // Implement active user counting logic
        return 0;
    }

    private long getActiveQuizCount() {
        // Implement active quiz counting logic
        return 0;
    }
}
