package com.example.demo.controller;

import com.example.demo.Dto.CompanyDTO;
import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyRestController {

    @Autowired
    private CompanyRepository companyRepository;

    // ✅ GET ALL COMPANIES
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = companyRepository.findAll()
                .stream()
                .map(CompanyService::toDTO)
                .toList();
        return ResponseEntity.ok(companies);
    }

    // ✅ GET COMPANY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable int id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return ResponseEntity.ok(CompanyService.toDTO(company));
    }

    // ✅ CREATE COMPANY
    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody Company company) {
        Company saved = companyRepository.save(company);
        return ResponseEntity.ok(CompanyService.toDTO(saved));
    }

    // ✅ UPDATE COMPANY
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable int id, @RequestBody Company updatedCompany) {
        // Lấy company từ DB
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Cập nhật tên công ty
        company.setCompanyName(updatedCompany.getCompanyName());

        // Cập nhật users nếu có, tránh null
        if (updatedCompany.getUsers() != null) {
            company.setUsers(updatedCompany.getUsers());
        }

        // Lưu vào DB
        Company saved = companyRepository.save(company);

        // Trả về DTO, đảm bảo users không null
        CompanyDTO dto = CompanyService.toDTO(saved);
        if (dto.getUsers() == null) {
            dto.setUsers(new ArrayList<>()); // tránh trả về null
        }

        return ResponseEntity.ok(dto);
    }

    // ✅ DELETE COMPANY
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable int id) {
        if (!companyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        companyRepository.deleteById(id);
        return ResponseEntity.ok("Company deleted successfully");
    }
}
