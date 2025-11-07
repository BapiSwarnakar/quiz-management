package com.stech.quiz.controller;

import com.stech.quiz.dto.PermissionDto;
import com.stech.quiz.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public String listPermissions(Model model) {
        List<PermissionDto> permissions = permissionService.getAllPermissions();
        model.addAttribute("permissions", permissions);
        return "admin/permissions/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("permission", new PermissionDto());
        return "admin/permissions/form";
    }

    @PostMapping("/save")
    public String savePermission(@Valid @ModelAttribute("permission") PermissionDto permissionDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/permissions/form";
        }
        
        try {
            permissionService.createPermission(permissionDto);
            redirectAttributes.addFlashAttribute("success", "Permission created successfully!");
            return "redirect:/admin/permissions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating permission: " + e.getMessage());
            return "redirect:/admin/permissions/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        PermissionDto permissionDto = permissionService.getPermissionById(id);
        model.addAttribute("permission", permissionDto);
        return "admin/permissions/form";
    }

    @PostMapping("/update/{id}")
    public String updatePermission(@PathVariable Long id,
                                 @Valid @ModelAttribute("permission") PermissionDto permissionDto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/permissions/form";
        }
        
        try {
            permissionService.updatePermission(id, permissionDto);
            redirectAttributes.addFlashAttribute("success", "Permission updated successfully!");
            return "redirect:/admin/permissions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating permission: " + e.getMessage());
            return "redirect:/admin/permissions/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePermission(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            permissionService.deletePermission(id);
            redirectAttributes.addFlashAttribute("success", "Permission deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting permission: " + e.getMessage());
        }
        return "redirect:/admin/permissions";
    }
    
    // Handle permission name check via AJAX
    @GetMapping("/check-name")
    @ResponseBody
    public boolean checkPermissionName(@RequestParam String name, @RequestParam(required = false) Long id) {
        if (id != null) {
            // For edit case, exclude current permission from the check
            PermissionDto existing = permissionService.getPermissionById(id);
            if (existing.getName().equals(name)) {
                return true; // Name hasn't changed, so it's valid
            }
        }
        return !permissionService.existsByName(name);
    }
}
