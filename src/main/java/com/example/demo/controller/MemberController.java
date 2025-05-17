package com.example.demo.controller;

import com.example.demo.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class MemberController {

    List<Member> members = Arrays.asList(
            new Member("Nguyễn Đức Mạnh",1,21,"manh99294@donga.edu.vn"),
            new Member("Nguyễn Đình Cang",2,21,"Cang@donga.edu.vn"),
            new Member("Huỳnh Hữu Nghĩa",3,21,"Nghia@donga.edu.vn")
    );
    @GetMapping("/members")
    public String showMembers(Model model) {
        model.addAttribute("members", members);
        return "printMember";
    }

    @GetMapping("/members/{id}")
    public String showMember(@PathVariable int id, Model model) {
        Member found = members.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
        model.addAttribute("member", found);
        return "member";
    }
}
