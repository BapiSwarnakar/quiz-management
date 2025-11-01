package com.stech.quiz.repository;

import com.stech.quiz.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findTop50ByOrderByTimestampDesc();
    
    List<UserActivity> findByUserIdOrderByTimestampDesc(Long userId);
}
