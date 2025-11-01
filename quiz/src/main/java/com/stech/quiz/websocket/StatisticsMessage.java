package com.stech.quiz.websocket;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StatisticsMessage {
    private String type;
    private Object payload;
    private LocalDateTime timestamp = LocalDateTime.now();
}
