package com.example.demo.service;

import com.example.demo.Dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.model.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class UserService {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());

        // Lấy tên công ty nếu có
        if (user.getCompany() != null) {
            dto.setCompanyName(user.getCompany().getCompanyName());
        }

        // Lấy danh sách role name
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);

        return dto;
    }
}
