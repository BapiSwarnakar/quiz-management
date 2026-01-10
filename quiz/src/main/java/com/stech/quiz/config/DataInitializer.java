package com.stech.quiz.config;

import com.stech.quiz.entity.Permission;
import com.stech.quiz.entity.Role;
import com.stech.quiz.entity.User;
import com.stech.quiz.repository.PermissionRepository;
import com.stech.quiz.repository.RoleRepository;
import com.stech.quiz.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // 1. Migrate Permissions
        List<String> permissionNames = Arrays.asList(
            "ADMINISTRATOR_DASHBOARD", "DASHBOARD_VIEW",
            "USER_VIEW", "USER_CREATE", "USER_EDIT", "USER_DELETE",
            "QUIZ_VIEW", "QUIZ_CREATE", "QUIZ_EDIT", "QUIZ_DELETE",
            "CATEGORY_VIEW", "CATEGORY_CREATE", "CATEGORY_EDIT", "CATEGORY_DELETE",
            "PERMISSION_VIEW", "PERMISSION_CREATE", "PERMISSION_EDIT", "PERMISSION_DELETE",
            "ROLE_VIEW", "ROLE_CREATE", "ROLE_UPDATE", "ROLE_DELETE",
            "QUESTION_READ", "QUESTION_CREATE", "QUESTION_UPDATE", "QUESTION_DELETE",
            "REPORT_VIEW", "REPORT_EXPORT"
        );

        for (String name : permissionNames) {
            if (!permissionRepository.findByName(name).isPresent()) {
                Permission permission = new Permission();
                permission.setName(name);
                permission.setDescription("Permission for " + name.toLowerCase().replace('_', ' '));
                permissionRepository.save(permission);
            }
        }
        permissionRepository.flush();

        // 2. Migrate Roles
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            return role;
        });
        
        // Sync all permissions to admin
        adminRole.setPermissions(new HashSet<>(permissionRepository.findAll()));
        roleRepository.saveAndFlush(adminRole);

        if (!roleRepository.findByName("ROLE_USER").isPresent()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.saveAndFlush(userRole);
        }

        // 3. Migrate Default Admin User
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@gmail.com");
            admin.setGender("Male");
            admin.setMobile("1234567890");
            admin.setPassword(passwordEncoder.encode("Test12@#"));
            
            // Assign ROLE_ADMIN
            admin.getRoles().add(adminRole);
            
            userRepository.save(admin);
        }
    }
}
