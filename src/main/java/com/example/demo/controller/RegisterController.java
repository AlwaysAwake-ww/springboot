package com.example.demo.controller;


import com.example.demo.domain.MemberDomain;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final MemberRepository memberRepository;

    @GetMapping("/register")
    public ModelAndView register(MemberDomain memberDomain, Authentication authentication){

        if(authentication==null)
            return new ModelAndView("register");

        return new ModelAndView("redirect:/user/home");
    }

    @PostMapping("/register")
    public ModelAndView registerAction(MemberDomain memberDomain){

        memberDomain.setMemberRole("ROLE_MEMBER");
        memberDomain.setMemberIntroduction("아직 자기소개가 없어요");
        memberDomain.setJoinDate(LocalDateTime.now().toLocalDate().toString());
        memberDomain.setMemberPassword(memberDomain.encrypt());


        memberRepository.save(memberDomain);
        return new ModelAndView("redirect:/login");
    }
}