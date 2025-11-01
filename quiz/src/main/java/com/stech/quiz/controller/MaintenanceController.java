package com.stech.quiz.controller;

import com.stech.quiz.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/maintenance")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    @GetMapping
    public String maintenanceDashboard(Model model) {
        model.addAttribute("status", maintenanceService.getSystemStatus());
        return "admin/maintenance";
    }

    @PostMapping("/backup")
    public String createBackup() {
        maintenanceService.performDailyMaintenance();
        return "redirect:/admin/maintenance?backup=success";
    }

    @PostMapping("/optimize")
    public String optimizeDatabase() {
        maintenanceService.optimizeDatabase();
        return "redirect:/admin/maintenance?optimize=success";
    }
}
