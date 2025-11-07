package com.stech.quiz.service;

import com.stech.quiz.dto.PermissionDto;
import java.util.List;

public interface PermissionService {
    PermissionDto createPermission(PermissionDto permissionDto);
    PermissionDto getPermissionById(Long id);
    PermissionDto getPermissionByName(String name);
    List<PermissionDto> getAllPermissions();
    PermissionDto updatePermission(Long id, PermissionDto permissionDto);
    void deletePermission(Long id);
    boolean existsByName(String name);
}
