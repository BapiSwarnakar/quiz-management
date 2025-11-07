package com.stech.quiz.repository;

import com.stech.quiz.entity.Role;
import com.stech.quiz.entity.RolePermission;
import com.stech.quiz.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId")
    List<RolePermission> findByRoleId(@Param("roleId") Long roleId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = :roleId AND rp.permission.id IN :permissionIds")
    void deleteByRoleIdAndPermissionIdIn(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
    
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
