//package com.example.demo.controller;
//
//import com.example.demo.model.User;
//import com.example.demo.service.CompanyService;
//import com.example.demo.service.UserService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/users")
//public class UserViewController {
//
//    private final UserService userService;
//    private final CompanyService companyService;
//
//    public UserViewController(UserService userService, CompanyService companyService) {
//        this.userService = userService;
//        this.companyService = companyService;
//    }
//
//    @GetMapping
//    public String listUsers(Model model) {
//        model.addAttribute("users", userService.getAllUsers());
//        return "user"; // => user.html
//    }
//
//    @GetMapping("/add")
//    public String showAddUserForm(Model model) {
//        model.addAttribute("user", new User());
//        model.addAttribute("companies", companyService.getAllCompanies());
//        return "addUser"; // => addUser.html
//    }
//
//    @PostMapping("/save")
//    public String saveUser(@ModelAttribute("user") User user) {
//        userService.saveUser(user);
//        return "redirect:/users";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String showEditUserForm(@PathVariable Long id, Model model) {
//        return userService.getUserById(id)
//                .map(user -> {
//                    model.addAttribute("user", user);
//                    model.addAttribute("companies", companyService.getAllCompanies());
//                    return "addUser"; // dùng lại form
//                })
//                .orElse("redirect:/users");
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return "redirect:/users";
//    }
//}
