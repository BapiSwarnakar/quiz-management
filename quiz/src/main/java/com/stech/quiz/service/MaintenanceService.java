package com.stech.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private final EntityManager entityManager;
    private final BackupService backupService;

    @Scheduled(cron = "0 0 1 * * ?") // Run at 1 AM every day
    public void performDailyMaintenance() {
        cleanupOldData();
        optimizeDatabase();
        backupService.createBackup();
    }

    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("lastBackup", getLastBackupTime());
        status.put("databaseSize", getDatabaseSize());
        status.put("uploadSize", getUploadDirectorySize());
        status.put("systemUptime", getSystemUptime());
        return status;
    }

    private void cleanupOldData() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        entityManager.createQuery("DELETE FROM UserActivity a WHERE a.timestamp < :date")
            .setParameter("date", threeMonthsAgo)
            .executeUpdate();
    }

    public void optimizeDatabase() {
        entityManager.createNativeQuery("OPTIMIZE TABLE quiz_results").executeUpdate();
        entityManager.createNativeQuery("OPTIMIZE TABLE user_activities").executeUpdate();
    }

    // Additional helper methods implementation
    private LocalDateTime getLastBackupTime() {
        // Implementation
        return LocalDateTime.now();
    }

    private long getDatabaseSize() {
        // Implementation
        return 0L;
    }

    private long getUploadDirectorySize() {
        // Implementation
        return 0L;
    }

    private String getSystemUptime() {
        // Implementation
        return "0 days, 0 hours";
    }
}
