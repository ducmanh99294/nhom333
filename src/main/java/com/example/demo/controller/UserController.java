package com.example.demo.controller;
import com.example.demo.model.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //hiển thị danh dsach thành viên
    @GetMapping("/")
    public String showMembers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "printMember";
    }

    //hiển thị chi tiết thành viên
@GetMapping("/member/{id}")
public String getMemberDetail(@PathVariable Long id, Model model) {
    User member = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + id));
    model.addAttribute("member", member);
    return "member";
}

    @GetMapping("/add-user")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/add-user?success";
    }
}
