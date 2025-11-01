package com.stech.quiz.service;

import com.stech.quiz.entity.QuizResult;
import com.stech.quiz.dto.QuizReportDTO;
import com.stech.quiz.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final QuizResultRepository resultRepository;
    
    public List<QuizReportDTO> generateQuizReports() {
        return resultRepository.findAllQuizReports().stream()
            .map(this::mapToQuizReportDTO)
            .collect(Collectors.toList());
    }

    public byte[] generateReportExcel() {
        // Implementation for Excel export
        return new byte[0];
    }

    public Map<String, Object> generateQuizReport(Long quizId) {
        List<QuizResult> results = resultRepository.findByQuizId(quizId);
        
        Map<String, Object> report = new HashMap<>();
        report.put("totalAttempts", results.size());
        report.put("averageScore", calculateAverageScore(results));
        report.put("passRate", calculatePassRate(results));
        report.put("scoreDistribution", calculateScoreDistribution(results));
        
        return report;
    }

    private double calculateAverageScore(List<QuizResult> results) {
        return results.stream()
                .mapToDouble(r -> (double) r.getObtainedMarks() / r.getTotalMarks() * 100)
                .average()
                .orElse(0.0);
    }

    private double calculatePassRate(List<QuizResult> results) {
        long passCount = results.stream()
                .filter(r -> (double) r.getObtainedMarks() / r.getTotalMarks() >= 0.4)
                .count();
        return results.isEmpty() ? 0 : (double) passCount / results.size() * 100;
    }

    private Map<String, Integer> calculateScoreDistribution(List<QuizResult> results) {
        Map<String, Integer> distribution = new HashMap<>();
        distribution.put("0-20", 0);
        distribution.put("21-40", 0);
        distribution.put("41-60", 0);
        distribution.put("61-80", 0);
        distribution.put("81-100", 0);
        
        for (QuizResult result : results) {
            double percentage = (double) result.getObtainedMarks() / result.getTotalMarks() * 100;
            if (percentage <= 20) {
                distribution.put("0-20", distribution.get("0-20") + 1);
            } else if (percentage <= 40) {
                distribution.put("21-40", distribution.get("21-40") + 1);
            } else if (percentage <= 60) {
                distribution.put("41-60", distribution.get("41-60") + 1);
            } else if (percentage <= 80) {
                distribution.put("61-80", distribution.get("61-80") + 1);
            } else {
                distribution.put("81-100", distribution.get("81-100") + 1);
            }
        }
        
        return distribution;
    }

    private QuizReportDTO mapToQuizReportDTO(Object[] result) {
        QuizReportDTO dto = new QuizReportDTO();
        // Mapping logic
        return dto;
    }
}
