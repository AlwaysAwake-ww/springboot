package com.example.demo.controller;


import com.example.demo.dto.MemberDetail;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.ImageService;
import com.example.demo.service.LoginService;
import com.example.demo.service.MemberDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberDetailService memberDetailService;

    @GetMapping("/login")
    public ModelAndView login(Authentication authentication, @RequestParam(value="error", required = false)boolean error, @RequestParam(value="exception", required = false)String exception, ModelAndView model, HttpServletRequest request){

        if(authentication!=null){
            model.setViewName("redirect:/user/home");
            return model;
        }

        // 세션에 값 전달하는 법
//        HttpSession session = request.getSession();
//        session.setAttribute("member", );
        model.addObject("error", error);
        model.addObject("exception", exception);
        model.setViewName("login");
        return model;
    }

    @GetMapping("/logout")
    public ModelAndView logout(@AuthenticationPrincipal MemberDetail memberDetail){


        memberDetailService.deleteTempImage(memberDetailService.getMemberDomain(memberDetail.getUsername()).getMemberIndex());

        return  new ModelAndView("redirect:/user/home");
    }

}
