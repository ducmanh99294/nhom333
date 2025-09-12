package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Tìm user bằng email (dùng cho login)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Lấy tất cả user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Lấy user theo ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // ✅ Lưu hoặc cập nhật user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // ✅ Xóa user theo ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
