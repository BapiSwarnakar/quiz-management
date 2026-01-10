package com.stech.quiz.security;

import com.stech.quiz.entity.Role;
import com.stech.quiz.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class UserPrincipal implements UserDetails {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String name, String email, String password,
                        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        log.info("Creating UserPrincipal for user: {}", user.getEmail());
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                // Add role itself
                String roleName = role.getName();
                if (!roleName.startsWith("ROLE_")) {
                    roleName = "ROLE_" + roleName;
                }
                authorities.add(new SimpleGrantedAuthority(roleName));
                log.info("Added Role Authority: {}", roleName);
                
                // Add permissions from this role
                if (role.getPermissions() != null) {
                    for (var permission : role.getPermissions()) {
                        String permName = permission.getName();
                        authorities.add(new SimpleGrantedAuthority(permName));
                        log.info("  Added Permission Authority from role {}: {}", roleName, permName);
                    }
                }
            }
        }

        List<GrantedAuthority> distinctAuthorities = authorities.stream()
                .distinct()
                .collect(Collectors.toList());

        log.info("Final authority list for {}: {}", user.getEmail(), 
                 distinctAuthorities.stream()
                     .map(GrantedAuthority::getAuthority)
                     .collect(Collectors.joining(", ")));

        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                distinctAuthorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
