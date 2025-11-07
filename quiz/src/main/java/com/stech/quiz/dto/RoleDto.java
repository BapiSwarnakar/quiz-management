package com.stech.quiz.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private Set<PermissionDto> permissions = new HashSet<>();
}
