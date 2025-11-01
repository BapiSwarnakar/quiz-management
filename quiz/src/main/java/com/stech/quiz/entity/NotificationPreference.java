package com.stech.quiz.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    private User user;
    
    private boolean emailNotifications = true;
    private boolean quizResultNotification = true;
    private boolean newQuizNotification = true;
    private boolean systemUpdates = false;
}
