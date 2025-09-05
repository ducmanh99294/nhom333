package com.example.demo.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "USER_DEMO")
@Entity
public class User {
    @Id
    @Column
    //1.  GenerationType.AUTO là kiểu generate primary key mặc định cho phép persistence provider
    // tự lựa chọn kiểu mà nó muốn.
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    // 2.GenerationType.IDENTITY là kiểu dễ sử dụng nhất nhưng về mặt hiệu năng thì nó không phải là một lựa chọn hàng đầu.
    // GenerationType.IDENTITY dựa trên một dữ liệu tăng dần (AUTO_INCREMENT) trong database,
    // cho phép database sinh một giá trị mới với mỗi thao tác insert.
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    //3. Đây là kiểu generate được Hibernate khuyến khích sử dụng, GenerationType.SEQUENCE
    // Hibernate sẽ tạo ra một bảng HIBERNATE_SEQUENCE trong database dùng để lưu trữ giá trị tiếp theo của primary key.
    // Nó sẽ thực thi một câu lệnh SQL SELECT để lấy giá trị tiếp theo trong bảng HIBERNATE_SEQUENCE
    // khi thực thi một câu lệnh SQL INSERT. Bởi vì giá trị tiếp theo có thể lấy được từ bảng HIBERNATE_SEQUENCE
    // nên các câu lệnh SQL Insert không cần thiết phải thực hiện ngay lập tức, điều này cho phép Hibernate
    // sử dụng tính năng JDBC Batch để tối ưu hóa hiệu xuất.
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id")
    private Company company;

    private String password;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
