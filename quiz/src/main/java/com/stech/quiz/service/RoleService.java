package com.stech.quiz.service;

import com.stech.quiz.dto.RoleDto;
import com.stech.quiz.entity.Role;
import com.stech.quiz.entity.Permission;
import com.stech.quiz.exception.ResourceNotFoundException;
import com.stech.quiz.repository.RoleRepository;
import com.stech.quiz.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto getRoleById(Long id);
    RoleDto getRoleByName(String name);
    List<RoleDto> getAllRoles();
    RoleDto updateRole(Long id, RoleDto roleDto);
    void deleteRole(Long id);
    RoleDto assignPermissionsToRole(Long roleId, Set<Long> permissionIds);
    boolean existsByName(String name);
}

@Service
@RequiredArgsConstructor
class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new IllegalArgumentException("Role with name " + roleDto.getName() + " already exists");
        }
        
        Role role = modelMapper.map(roleDto, Role.class);
        Role savedRole = roleRepository.save(role);
        return modelMapper.map(savedRole, RoleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name));
        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        
        existingRole.setName(roleDto.getName());
        
        if (roleDto.getPermissions() != null) {
            Set<Permission> permissions = roleDto.getPermissions().stream()
                    .map(permissionDto -> modelMapper.map(permissionDto, Permission.class))
                    .collect(Collectors.toSet());
            existingRole.setPermissions(permissions);
        }
        
        Role updatedRole = roleRepository.save(existingRole);
        return modelMapper.map(updatedRole, RoleDto.class);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoleDto assignPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        // Fetch the role with its permissions in a single query
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        
        // Clear existing permissions
        role.getPermissions().clear();
        
        if (permissionIds != null && !permissionIds.isEmpty()) {
            // Fetch all permissions in a single query
            Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllById(permissionIds)
            );
            
            // Check if all requested permissions were found
            if (permissions.size() != permissionIds.size()) {
                Set<Long> foundIds = permissions.stream()
                    .map(Permission::getId)
                    .collect(Collectors.toSet());
                
                Set<Long> missingIds = permissionIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());
                    
                throw new ResourceNotFoundException("Permissions not found with ids: " + missingIds);
            }
            
            // Update the permissions
            role.setPermissions(permissions);
            
            // Update the inverse side of the relationship
            for (Permission permission : permissions) {
                permission.getRoles().add(role);
            }
        }
        
        // Save the updated role
        Role updatedRole = roleRepository.save(role);
        
        // Ensure changes are flushed to the database
        roleRepository.flush();
        
        return modelMapper.map(updatedRole, RoleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}
