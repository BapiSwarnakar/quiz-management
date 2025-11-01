package com.stech.quiz.service;

import com.stech.quiz.entity.Role;
import com.stech.quiz.entity.Permission;
import com.stech.quiz.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role createRole(String name, Set<Permission> permissions) {
        Role role = new Role();
        role.setName(name);
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    @Transactional
    public void updateRolePermissions(Long roleId, Set<Permission> permissions) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
}
