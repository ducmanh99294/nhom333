package com.example.demo.controller;

import com.example.demo.model.Company;
import com.example.demo.services.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Integer id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/companies")
    public Company createCompany(@RequestBody Company company) {
        return companyService.saveCompany(company);
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Integer id, @RequestBody Company companyDetails) {
        return companyService.getCompanyById(id)
                .map(company -> {
                    company.setCompanyName(companyDetails.getCompanyName());
                    company.setUsers(companyDetails.getUsers());
                    return ResponseEntity.ok(companyService.saveCompany(company));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @Controller
    class CompanyViewController {

    private final CompanyService companyService;

    public CompanyViewController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Danh sách company
    @GetMapping("/company")
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "company"; // company.html
    }

    @GetMapping("/company/{id}/users")
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
    @GetMapping("/company/add")
    public String showAddCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "addCompany";
    }

    // Lưu company (cả add & edit)
    @PostMapping("/company/save")
    public String saveCompany(@ModelAttribute("company") Company company) {
        companyService.saveCompany(company);
        return "redirect:/company";
    }

    // Hiển thị form edit
    @GetMapping("/company/edit/{id}")
    public String showEditCompanyForm(@PathVariable Integer id, Model model) {
        return companyService.getCompanyById(id)
                .map(c -> {
                    model.addAttribute("company", c);
                    return "addCompany";
                })
                .orElse("redirect:/company");
    }

    // Xóa company
    @GetMapping("/company/delete/{id}")
    public String deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
        return "redirect:/company";
    }
}
}
