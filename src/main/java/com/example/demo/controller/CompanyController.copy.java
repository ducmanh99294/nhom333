package com.example.demo.controller;

import com.example.demo.model.Company;
import com.example.demo.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Controller
    class CompanyViewController {

    private final CompanyService companyService;

    public CompanyViewController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Danh sách company
    @GetMapping("/")
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "company"; 
    }

    @GetMapping("/{id}/users")
    public String viewCompanyUsers(@PathVariable Integer id, Model model) {
        return companyService.getCompanyById(id)
                .map(company -> {
                    model.addAttribute("company", company);
                    model.addAttribute("users", company.getUsers()); // users thuộc company
                    return "user";
                })
                .orElse("redirect:/companies-page");
    }

    // Hiển thị form thêm mới
    @GetMapping("/add")
    public String showAddCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "addCompany";
    }

    // Lưu company (cả add & edit)
    @PostMapping("/save")
    public String saveCompany(@ModelAttribute("company") Company company) {
        companyService.saveCompany(company);
        return "redirect:/company";
    }

    // Hiển thị form edit
    @GetMapping("/edit/{id}")
    public String showEditCompanyForm(@PathVariable Integer id, Model model) {
        return companyService.getCompanyById(id)
                .map(c -> {
                    model.addAttribute("company", c);
                    return "addCompany";
                })
                .orElse("redirect:/company");
    }

    // Xóa company
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
        return "redirect:/company";
    }
}
}
