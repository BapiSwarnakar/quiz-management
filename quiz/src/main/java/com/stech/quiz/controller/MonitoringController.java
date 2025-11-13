package com.stech.quiz.controller;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import com.stech.quiz.dto.SystemMetricsDto;
import com.stech.quiz.monitoring.QuizMetricsCollector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/monitoring")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class MonitoringController {
    private final QuizMetricsCollector metricsCollector;
    private static final Random RANDOM = new Random();
    private static final OperatingSystemMXBean OS_BEAN = ManagementFactory.getOperatingSystemMXBean();

    @GetMapping
    public String showDashboard(Model model) {
        model.addAttribute("metrics", getSystemMetrics());
        model.addAttribute("recentActivities", generateRecentActivities());
        return "admin/monitoring/dashboard";
    }

    private SystemMetricsDto getSystemMetrics() {
        // Get basic metrics from QuizMetricsCollector
        Map<String, Object> basicMetrics = metricsCollector.getMetrics();
        
        // Generate time labels for the last 7 days
        List<String> timeLabels = IntStream.range(0, 7)
                .mapToObj(i -> LocalDateTime.now().minusDays(6 - i).toLocalDate().toString())
                .collect(Collectors.toList());
        
        // Generate some sample data for the charts
        List<Long> quizStarts = timeLabels.stream()
                .map(day -> (long) (RANDOM.nextInt(50) + 20))
                .collect(Collectors.toList());
                
        List<Long> quizCompletions = timeLabels.stream()
                .map(day -> (long) (RANDOM.nextInt(40) + 15))
                .collect(Collectors.toList());
        
        // Get system metrics
        double cpuLoad = OS_BEAN.getSystemLoadAverage() * 10; // Convert to percentage
        if (cpuLoad < 0) cpuLoad = RANDOM.nextDouble() * 30 + 10; // Fallback if not available
        
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / (1024 * 1024); // MB
        long freeMemory = runtime.freeMemory() / (1024 * 1024); // MB
        long usedMemory = totalMemory - freeMemory;
        double memoryUsagePercent = (usedMemory / (double) totalMemory) * 100;
        
        // For demo purposes, generate some disk usage metrics
        double totalDisk = 500; // GB
        double usedDisk = 150 + RANDOM.nextDouble() * 50; // GB
        double diskUsagePercent = (usedDisk / totalDisk) * 100;
        
        return SystemMetricsDto.builder()
                .activeUsers(RANDOM.nextInt(50) + 10)
                .quizzesToday(quizStarts.get(quizStarts.size() - 1).intValue())
                .avgResponseTime(Math.round(RANDOM.nextDouble() * 10 + 50)) // 50-60ms
                .cpuUsage(Math.min(100, Math.max(0, cpuLoad))) // Cap at 100%
                .memoryUsage(usedMemory)
                .memoryUsagePercent(memoryUsagePercent)
                .diskUsage(usedDisk)
                .diskUsagePercent(diskUsagePercent)
                .timeLabels(timeLabels)
                .quizStarts(quizStarts)
                .quizCompletions(quizCompletions)
                .build();
    }
    
    @GetMapping("/export")
    @ResponseBody
    public void exportMetrics(HttpServletResponse response) throws Exception {
        // Set response headers
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        response.setContentType("text/csv");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", 
            "attachment; filename=\"monitoring_export_" + timestamp + ".csv\"");

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                response.getOutputStream(), StandardCharsets.UTF_8))) {
            
            // Write CSV header
            writer.println("Metric,Value,Unit");
            
            // Get current metrics
            SystemMetricsDto metrics = getSystemMetrics();
            
            // Write system metrics
            writer.println(String.format("Active Users,%d,users", metrics.getActiveUsers()));
            writer.println(String.format("Quizzes Today,%d,quizzes", metrics.getQuizzesToday()));
            writer.println(String.format("Avg. Response Time,%.2f,ms", metrics.getAvgResponseTime()));
            writer.println(String.format("CPU Usage,%.2f,%%", metrics.getCpuUsage()));
            writer.println(String.format("Memory Usage,%.2f,MB", metrics.getMemoryUsage()));
            writer.println(String.format("Memory Usage,%.2f,%%", metrics.getMemoryUsagePercent()));
            writer.println(String.format("Disk Usage,%.2f,GB", metrics.getDiskUsage()));
            writer.println(String.format("Disk Usage,%.2f,%%", metrics.getDiskUsagePercent()));
            
            // Write quiz activity data
            writer.println("\nQuiz Activity (Last 7 Days)");
            writer.println("Date,Quizzes Started,Quizzes Completed");
            for (int i = 0; i < metrics.getTimeLabels().size(); i++) {
                writer.println(String.format("%s,%d,%d",
                    metrics.getTimeLabels().get(i),
                    metrics.getQuizStarts().get(i),
                    metrics.getQuizCompletions().get(i)));
            }
            
            // Write recent activities
            writer.println("\nRecent Activities");
            writer.println("Timestamp,Event,User,Status");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (SystemMetricsDto.ActivityDto activity : generateRecentActivities()) {
                writer.println(String.format("%s,%s,%s,%s",
                    activity.getTimestamp().format(formatter),
                    escapeCsv(activity.getEvent()),
                    activity.getUser(),
                    activity.getStatus()));
            }
        }
    }
    
    private String escapeCsv(String input) {
        if (input == null) {
            return "";
        }
        // Escape quotes and wrap in quotes if contains comma or newline
        String escaped = input.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
    
    private List<SystemMetricsDto.ActivityDto> generateRecentActivities() {
        String[] events = {
            "User logged in",
            "Quiz started",
            "Quiz completed",
            "User registered",
            "Password changed",
            "Profile updated"
        };
        
        String[] users = {
            "admin@example.com",
            "user1@example.com",
            "user2@example.com",
            "test@example.com"
        };
        
        String[] statuses = {"SUCCESS", "WARNING", "ERROR"};
        
        return IntStream.range(0, 10)
                .mapToObj(i -> {
                    LocalDateTime timestamp = LocalDateTime.now().minusMinutes(RANDOM.nextInt(1440)); // Within last 24 hours
                    String event = events[RANDOM.nextInt(events.length)];
                    String user = users[RANDOM.nextInt(users.length)];
                    String status = statuses[RANDOM.nextInt(statuses.length)];
                    
                    return SystemMetricsDto.ActivityDto.builder()
                            .timestamp(timestamp)
                            .event(event)
                            .user(user)
                            .status(status)
                            .build();
                })
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp())) // Most recent first
                .collect(Collectors.toList());
    }
}
