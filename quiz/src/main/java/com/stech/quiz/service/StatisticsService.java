package com.stech.quiz.service;

import com.stech.quiz.dto.UserStatistics;
import com.stech.quiz.entity.QuizResult;
import com.stech.quiz.repository.QuizResultRepository;
import com.stech.quiz.websocket.StatisticsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final SimpMessagingTemplate messagingTemplate;
    private final QuizResultRepository resultRepository;

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void broadcastStatistics() {
        StatisticsMessage message = new StatisticsMessage();
        message.setType("STATS_UPDATE");
        message.setPayload(collectStatistics());
        messagingTemplate.convertAndSend("/topic/statistics", message);
    }

    private Map<String, Object> collectStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activeUsers", getActiveUserCount());
        stats.put("totalQuizzes", getTotalQuizCount());
        stats.put("recentResults", getRecentResults());
        return stats;
    }

    public List<QuizResult> getRecentResults() {
        return resultRepository.findTop10ByOrderByEndTimeDesc();
    }

    public UserStatistics getUserStatistics(Long userId) {
        UserStatistics stats = new UserStatistics();
        List<QuizResult> allResults = resultRepository.findByUserId(userId);
        List<QuizResult> recentResults = resultRepository.findTop5ByUserIdOrderByEndTimeDesc(userId);
        
        stats.setTotalQuizzes(allResults.size());
        stats.setRecentResults(recentResults);
        
        if (!allResults.isEmpty()) {
            double avgScore = allResults.stream()
                .mapToDouble(r -> (double) r.getObtainedMarks() / r.getTotalMarks() * 100)
                .average()
                .orElse(0.0);
            
            long totalMinutes = allResults.stream()
                .mapToLong(r -> java.time.Duration.between(r.getStartTime(), r.getEndTime()).toMinutes())
                .sum();
                
            stats.setAverageScore(avgScore);
            stats.setTotalTimeSpent(totalMinutes);
        }
        
        return stats;
    }

    public long getActiveUserCount() {
        return resultRepository.countByStatus("ACTIVE");
    }

    public long getTotalQuizCount() {
        return resultRepository.countAll();
    }
}
