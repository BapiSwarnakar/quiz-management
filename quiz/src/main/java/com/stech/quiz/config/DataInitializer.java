package com.stech.quiz.config;

import com.stech.quiz.entity.Permission;
import com.stech.quiz.entity.Role;
import com.stech.quiz.repository.PermissionRepository;
import com.stech.quiz.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        // Create DASHBOARD_VIEW permission if it doesn't exist
        Permission dashboardView = permissionRepository.findByName("DASHBOARD_VIEW")
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName("DASHBOARD_VIEW");
                    permission.setDescription("Allows access to the dashboard");
                    return permissionRepository.save(permission);
                });

        // Ensure ROLE_ADMIN exists and has the DASHBOARD_VIEW permission
        roleRepository.findByName("ROLE_ADMIN")
                .ifPresentOrElse(
                        adminRole -> {
                            if (adminRole.getPermissions().stream()
                                    .noneMatch(p -> p.getName().equals("DASHBOARD_VIEW"))) {
                                adminRole.getPermissions().add(dashboardView);
                                roleRepository.save(adminRole);
                            }
                        },
                        () -> {
                            Role adminRole = new Role();
                            adminRole.setName("ROLE_ADMIN");
                            adminRole.setPermissions(new HashSet<>(Arrays.asList(dashboardView)));
                            roleRepository.save(adminRole);
                        }
                );
    }
}
