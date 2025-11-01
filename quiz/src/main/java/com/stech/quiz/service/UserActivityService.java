package com.stech.quiz.service;

import com.stech.quiz.entity.UserActivity;
import com.stech.quiz.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserActivityRepository activityRepository;

    public List<UserActivity> getRecentActivities() {
        return activityRepository.findTop50ByOrderByTimestampDesc();
    }

    public List<UserActivity> getUserActivities(Long userId) {
        return activityRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalActivities = activityRepository.count();
        stats.put("totalActivities", totalActivities);
        return stats;
    }
}
