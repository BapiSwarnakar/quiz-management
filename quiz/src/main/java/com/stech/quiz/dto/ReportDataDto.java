package com.stech.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDataDto {
    private String title;
    private String description;
    private LocalDateTime generatedAt;
    private Map<String, Object> metrics;
    private List<ReportChartDto> charts;
    private List<ReportTableDto> tables;
    
    @Data
    @Builder
    public static class ReportChartDto {
        private String id;
        private String title;
        private String type; // line, bar, pie, etc.
        private Map<String, Object> data;
        private Map<String, Object> options;
    }
    
    @Data
    @Builder
    public static class ReportTableDto {
        private String id;
        private String title;
        private List<String> headers;
        private List<List<Object>> rows;
        private Map<String, Object> footer;
    }
}
