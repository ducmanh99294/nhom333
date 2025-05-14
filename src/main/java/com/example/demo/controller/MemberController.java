package com.example.demo.controller;

import com.example.demo.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class MemberController {

    @GetMapping("/members")
    public String showMembers(Model model) {
        List<Member> members = Arrays.asList(
                new Member("Nguyễn Đức Mạnh"),
                new Member("Nguyễn Đình Cang "),
                new Member("Huỳnh Hữu Nghĩa")
        );
        model.addAttribute("members", members);
        return "printMember"; // Trả về file templates/members.html
    }
}
