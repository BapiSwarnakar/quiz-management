package com.stech.quiz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_activities")
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    
    private String activityType;
    private String description;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String ipAddress;
    private String browser;
}
