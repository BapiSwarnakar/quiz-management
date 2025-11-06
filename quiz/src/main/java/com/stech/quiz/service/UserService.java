package com.stech.quiz.service;

import com.stech.quiz.entity.User;
import com.stech.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${upload.dir:uploads/photos/}")
    private String uploadDir;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(email);
    }

    public void updateProfile(User user, MultipartFile photo) {
        User existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setName(user.getName());
        existingUser.setMobile(user.getMobile());
        existingUser.setGender(user.getGender());
        // Save uploaded photo (if provided) and set photoUrl
        if (photo != null && !photo.isEmpty()) {
            try {
                // Resolve absolute upload directory
                Path baseUploadPath = Paths.get(uploadDir);
                if (!baseUploadPath.isAbsolute()) {
                    baseUploadPath = Paths.get("").toAbsolutePath().resolve(uploadDir).normalize();
                }
                if (!Files.exists(baseUploadPath)) {
                    Files.createDirectories(baseUploadPath);
                }
                String original = photo.getOriginalFilename();
                String ext = "";
                if (original != null && original.lastIndexOf('.') > -1) {
                    ext = original.substring(original.lastIndexOf('.'));
                }
                String filename = UUID.randomUUID() + (ext != null ? ext : "");
                Path target = baseUploadPath.resolve(filename).normalize();
                // Use Files.copy for better reliability across environments
                try (var in = photo.getInputStream()) {
                    java.nio.file.Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
                // Build URL path for serving (mapped by WebMvcConfig to /uploads/**)
                String normalizedDir = uploadDir.replace("\\", "/");
                if (!normalizedDir.endsWith("/")) normalizedDir += "/";
                String publicUrl = "/uploads/" + normalizedDir.replaceFirst("^uploads/", "");
                existingUser.setPhotoUrl(publicUrl + filename);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save profile photo", e);
            }
        }
        userRepository.save(existingUser);
    }
}