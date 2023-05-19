package com.example.demo.controller;


import com.example.demo.dto.MemberDetail;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final MemberDetailService memberDetailService;

    @GetMapping("/")
    public ModelAndView goHome(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }

    @GetMapping("/user/home")
    public ModelAndView main(Authentication authentication, @AuthenticationPrincipal MemberDetail memberDetail, ModelAndView model){

        // 로그인  안되어있으면 login URL redirect

//        if(authentication==null){
//            model.setViewName("redirect:/login");
//        }
//
//        // 로그인 되어있으면 /user/home redirect
//        else{
//            model.addObject("member",memberDetailService.getMember(memberDetail.getUsername()));
//            model.setViewName("home");
//        }
        model.addObject("member",memberDetailService.getMember(memberDetail.getUsername()));
        model.setViewName("home");
        return model;
    }
}
