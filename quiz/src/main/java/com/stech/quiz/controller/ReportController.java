package com.stech.quiz.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stech.quiz.dto.ReportDataDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('REPORT_VIEW', 'REPORT_EXPORT', 'REPORT_ANALYTICS')")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("active", "reports");
        model.addAttribute("pageTitle", "Reports");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT_VIEW')")
    public String showOverview(
            @RequestParam(value = "timeframe", defaultValue = "7") int timeframe,
            Model model) {
        
        // Sample data - replace with actual data from your services
        ReportDataDto reportData = ReportDataDto.builder()
                .title("Overview Report")
                .description("Summary of key metrics and activities")
                .generatedAt(LocalDateTime.now())
                .metrics(Map.of(
                        "totalQuizzes", 1250,
                        "activeUsers", 342,
                        "completionRate", 78.5,
                        "avgScore", 82.3
                ))
                .build();
        
        model.addAttribute("activeSub", "overview");
        model.addAttribute("reportData", reportData);
        model.addAttribute("timeframe", timeframe);
        model.addAttribute("pageTitle", "Overview");
        
        return "admin/reports/overview";
    }

    @GetMapping("/performance")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT_VIEW')")
    public String showPerformanceReport(
            @RequestParam(value = "timeframe", defaultValue = "30") int timeframe,
            Model model) {
        
        // Sample performance data
        ReportDataDto.ReportChartDto performanceChart = ReportDataDto.ReportChartDto.builder()
                .id("performanceChart")
                .title("Performance Over Time")
                .type("line")
                .data(Map.of(
                        "labels", List.of("Week 1", "Week 2", "Week 3", "Week 4"),
                        "datasets", List.of(
                                Map.of(
                                        "label", "Avg. Score",
                                        "data", List.of(75, 78, 82, 85),
                                        "borderColor", "#4e73df"
                                ),
                                Map.of(
                                        "label", "Completion Rate",
                                        "data", List.of(65, 72, 80, 85),
                                        "borderColor", "#1cc88a"
                                )
                        )
                ))
                .build();
        
        ReportDataDto reportData = ReportDataDto.builder()
                .title("Performance Report")
                .description("Detailed performance metrics and trends")
                .generatedAt(LocalDateTime.now())
                .charts(List.of(performanceChart))
                .metrics(Map.of(
                    "avgCompletionTime", "12:34",
                    "successRate", 87.0,
                    "avgScore", 82.5,
                    "totalAttempts", 1245
                ))
                .build();
        
        model.addAttribute("activeSub", "performance");
        model.addAttribute("reportData", reportData);
        model.addAttribute("timeframe", timeframe);
        model.addAttribute("pageTitle", "Performance");
        
        return "admin/reports/performance";
    }

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT_ANALYTICS')")
    public String showAnalytics(
            @RequestParam(value = "timeframe", defaultValue = "90") int timeframe,
            Model model) {
        
        // Sample analytics data
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalQuizzes", 1250);
        metrics.put("activeUsers", 342);
        metrics.put("avgCompletionRate", 78.5);
        metrics.put("avgScore", 82.3);
        
        model.addAttribute("active", "reports");
        model.addAttribute("activeSub", "analytics");
        model.addAttribute("timeframe", timeframe);
        model.addAttribute("pageTitle", "Analytics");
        
        return "admin/reports/analytics";
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT_EXPORT')")
    public String showExportPage(Model model) {
        model.addAttribute("active", "reports");
        model.addAttribute("activeSub", "export");
        model.addAttribute("pageTitle", "Export Data");
        model.addAttribute("exportFormats", List.of("CSV", "PDF", "Excel"));
        model.addAttribute("timeframeOptions", Map.of(
                "7", "Last 7 days",
                "30", "Last 30 days",
                "90", "Last 90 days",
                "365", "This year"
        ));
        
        return "admin/reports/export";
    }

}
