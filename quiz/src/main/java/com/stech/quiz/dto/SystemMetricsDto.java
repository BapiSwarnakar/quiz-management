package com.stech.quiz.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemMetricsDto {
    private int activeUsers;
    private int quizzesToday;
    private double avgResponseTime; // in ms
    private double cpuUsage; // percentage
    private double memoryUsage; // in MB
    private double memoryUsagePercent; // percentage
    private double diskUsage; // in GB
    private double diskUsagePercent; // percentage
    private List<String> timeLabels;
    private List<Long> quizStarts;
    private List<Long> quizCompletions;
    private List<ActivityDto> recentActivities;

    @Data
    @Builder
    public static class ActivityDto {
        private LocalDateTime timestamp;
        private String event;
        private String user;
        private String status; // SUCCESS, WARNING, ERROR
    }
}
