package com.stech.quiz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "role_permissions")
public class RolePermission {
    
    @EmbeddedId
    private RolePermissionId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    private Role role;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    private Permission permission;
    
    // Constructors, equals, and hashCode
    public RolePermission() {}
    
    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
        this.id = new RolePermissionId(role.getId(), permission.getId());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolePermission)) return false;
        RolePermission that = (RolePermission) o;
        return Objects.equals(role, that.role) &&
               Objects.equals(permission, that.permission);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(role, permission);
    }
}
