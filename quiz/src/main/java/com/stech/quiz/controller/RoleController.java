package com.stech.quiz.controller;

import com.stech.quiz.dto.RoleDto;
import com.stech.quiz.entity.Role;
import com.stech.quiz.entity.Permission;
import com.stech.quiz.exception.ResourceNotFoundException;
import com.stech.quiz.repository.PermissionRepository;
import com.stech.quiz.repository.RoleRepository;
import com.stech.quiz.service.RoleService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @GetMapping
    public String listRoles(Model model) {
        List<RoleDto> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "admin/roles/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new RoleDto());
        return "admin/roles/form";
    }

    @PostMapping("/save")
    public String saveRole(@Valid @ModelAttribute("role") RoleDto roleDto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/roles/form";
        }
        
        try {
            roleService.createRole(roleDto);
            redirectAttributes.addFlashAttribute("success", "Role created successfully!");
            return "redirect:/admin/roles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating role: " + e.getMessage());
            return "redirect:/admin/roles/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        RoleDto roleDto = roleService.getRoleById(id);
        model.addAttribute("role", roleDto);
        return "admin/roles/form";
    }

    @PostMapping("/update/{id}")
    public String updateRole(@PathVariable Long id,
                           @Valid @ModelAttribute("role") RoleDto roleDto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/roles/form";
        }
        
        try {
            roleService.updateRole(id, roleDto);
            redirectAttributes.addFlashAttribute("success", "Role updated successfully!");
            return "redirect:/admin/roles";
        } catch (Exception e) {
            log.error("Error updating role: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error updating role: " + e.getMessage());
            return "redirect:/admin/roles/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("success", "Role deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting role: " + e.getMessage());
        }
        return "redirect:/admin/roles";
    }

    @GetMapping("/{id}/permissions")
    public String showAssignPermissionsForm(@PathVariable Long id, Model model) {
        // Get role with permissions initialized
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        
        // Force initialize the permissions collection
        if (role.getPermissions() != null) {
            role.getPermissions().size(); // This initializes the collection
        }
        
        // Get all available permissions
        List<Permission> allPermissions = permissionRepository.findAll();
        
        model.addAttribute("role", role);
        model.addAttribute("allPermissions", allPermissions);
        return "admin/roles/assign-permissions";
    }

    @PostMapping("/{roleId}/permissions/update")
    @Transactional
    public String updateRolePermissions(
            @PathVariable Long roleId,
            @RequestParam(value = "permissionIds", required = false) List<Long> permissionIdsParam,
            RedirectAttributes redirectAttributes) {
        
        // Create a final copy of the permission IDs to use in the lambda
        final List<Long> permissionIds = permissionIdsParam != null ? 
            new ArrayList<>(permissionIdsParam) : new ArrayList<>();
        
        log.info("Updating permissions for role ID: {}", roleId);
        log.info("Selected permission IDs: {}", permissionIds);
        
        try {
            // Get the role with its current permissions
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
            
            // Get all available permissions
            List<Permission> allPermissions = permissionRepository.findAll();
            
            // Clear existing permissions
            role.getPermissions().clear();
            
            // Add selected permissions
            if (!permissionIds.isEmpty()) {
                Set<Permission> selectedPermissions = allPermissions.stream()
                    .filter(p -> permissionIds.contains(p.getId()))
                    .collect(Collectors.toSet());
                role.getPermissions().addAll(selectedPermissions);
                log.info("Added {} permissions to role '{}'", selectedPermissions.size(), role.getName());
                
                // Check for ADMINISTRATOR_DASHBOARD permission
                boolean hasAdminDashboard = selectedPermissions.stream()
                    .anyMatch(p -> "ADMINISTRATOR_DASHBOARD".equals(p.getName()));
                    
                // Ensure role name starts with ROLE_ if it has ADMINISTRATOR_DASHBOARD
                if (hasAdminDashboard && !role.getName().startsWith("ROLE_")) {
                    String newRoleName = "ROLE_" + role.getName();
                    log.info("Updating role name to '{}' due to ADMINISTRATOR_DASHBOARD permission", newRoleName);
                    role.setName(newRoleName);
                }
            }
            
            // Save the updated role
            role = roleRepository.saveAndFlush(role);
            log.info("Successfully updated role '{}' with {} permissions", 
                   role.getName(), role.getPermissions().size());
            
            redirectAttributes.addFlashAttribute("success", "Permissions updated successfully!");
            return "redirect:/admin/roles/" + roleId + "/permissions";
        } catch (Exception e) {
            String errorMsg = "Error updating permissions: " + e.getMessage();
            log.error(errorMsg, e);
            redirectAttributes.addFlashAttribute("error", errorMsg);
        }
        return "redirect:/admin/roles/edit/" + roleId;
    }
    
    @GetMapping("/check-name")
    @ResponseBody
    public boolean checkRoleName(@RequestParam String name, @RequestParam(required = false) Long id) {
        if (id != null) {
            RoleDto existing = roleService.getRoleById(id);
            if (existing.getName().equals(name)) {
                return true; // Name hasn't changed
            }
        }
        return !roleService.existsByName(name);
    }
}
