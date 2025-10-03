package com.example.demo.controller;

import com.example.demo.Dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // ✅ GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(UserService::toDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(UserService.toDTO(user));
    }

    // ✅ CREATE USER
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity.ok(UserService.toDTO(saved));
    }

    // ✅ UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getFirstName() != null) user.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null) user.setLastName(updatedUser.getLastName());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null) user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        if (updatedUser.getCompany() != null) user.setCompany(updatedUser.getCompany());

        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
                Set<Role> roles = updatedUser.getRoles().stream()
                        .map(r -> roleRepository.findByName(r.getName())
                                .orElseThrow(() -> new RuntimeException("Role not found: " + r.getName())))
                        .collect(Collectors.toSet());
                user.setRoles(roles);
            }

            User saved = userRepository.save(user);
            return ResponseEntity.ok(UserService.toDTO(saved));
        }

    // ✅ DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        // Lấy user từ database
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().clear();
        userRepository.delete(user);

        return ResponseEntity.ok(Map.of("message", "Xoá user thành công"));
    }
}
