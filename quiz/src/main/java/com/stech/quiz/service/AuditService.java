package com.stech.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.stech.quiz.entity.User;
import com.stech.quiz.entity.UserActivity;
import com.stech.quiz.repository.UserActivityRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final UserActivityRepository activityRepository;

    public void logActivity(String activityType, String description, User user) {
        UserActivity activity = new UserActivity();
        activity.setActivityType(activityType);
        activity.setDescription(description);
        activity.setUser(user);
        activity.setTimestamp(LocalDateTime.now());
        
        activityRepository.save(activity);
    }
}
