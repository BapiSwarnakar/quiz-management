package com.stech.quiz.service.impl;

import com.stech.quiz.dto.PermissionDto;
import com.stech.quiz.entity.Permission;
import com.stech.quiz.exception.ResourceNotFoundException;
import com.stech.quiz.repository.PermissionRepository;
import com.stech.quiz.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PermissionDto createPermission(PermissionDto permissionDto) {
        Permission permission = modelMapper.map(permissionDto, Permission.class);
        Permission savedPermission = permissionRepository.save(permission);
        return modelMapper.map(savedPermission, PermissionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDto getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
        return modelMapper.map(permission, PermissionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDto getPermissionByName(String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with name: " + name));
        return modelMapper.map(permission, PermissionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permission -> modelMapper.map(permission, PermissionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return permissionRepository.existsByName(name);
    }
    
    @Override
    @Transactional
    public PermissionDto updatePermission(Long id, PermissionDto permissionDto) {
        // Check if permission exists
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
        
        // Check if name is being changed and if the new name already exists
        if (!existingPermission.getName().equals(permissionDto.getName()) && 
            permissionRepository.existsByName(permissionDto.getName())) {
            throw new IllegalArgumentException("A permission with the name '" + permissionDto.getName() + "' already exists.");
        }
        
        // Update the permission
        existingPermission.setName(permissionDto.getName());
        existingPermission.setDescription(permissionDto.getDescription());
        
        Permission updatedPermission = permissionRepository.save(existingPermission);
        return modelMapper.map(updatedPermission, PermissionDto.class);
    }
}
