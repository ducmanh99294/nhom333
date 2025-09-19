package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

import java.util.Set;

@RestController
public class AutheController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        System.out.println("Email nhận được: " + loginRequest.getEmail());
        System.out.println("Password nhận được: " + loginRequest.getPassword());

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai mật khẩu");
        }

        String role = user.getRoles().iterator().next().getName();
        String token = jwtUtil.generateToken(user.getEmail(), role);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "role", role
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email đã tồn tại!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER không tồn tại"));
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);

        return ResponseEntity.ok("Đăng ký thành công");
    }

}

}