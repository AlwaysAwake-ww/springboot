package com.example.demo.controller;


import com.example.demo.domain.MemberDomain;
import com.example.demo.dto.Member;
import com.example.demo.dto.MemberDetail;
import com.example.demo.service.MemberDetailService;
import com.example.demo.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberDetailService memberDetailService;

    @GetMapping("/user/mypage")
    public ModelAndView mypage(@AuthenticationPrincipal MemberDetail memberDetail){

        Member member = memberDetailService.getMember(memberDetail.getUsername());



        ModelAndView model = new ModelAndView("mypage");
        model.addObject("member", member);
        return model;
    }


    @ResponseBody
    @PostMapping("/user/search")
    public List<MemberDomain> searchMember(@AuthenticationPrincipal MemberDetail memberDetail, @RequestParam(value = "searchCategory") String searchCategory, @RequestParam(value = "searchKeyword") String searchKeyword){



        try{

            List<MemberDomain> memberDomainList = memberDetailService.findByKeyword(searchCategory, searchKeyword);



            return memberDomainList;
        }
        catch(Exception e){
            e.getStackTrace();
            return null;
        }
    }

    @ResponseBody
    @PostMapping("/user/mypage/update")
    public String introductionUpdate(@RequestParam("introduction") String introduction, @AuthenticationPrincipal MemberDetail memberDetail){

        try{
            MemberDomain memberDomain = memberDetail.getMemberDomain();
            memberDomain.setMemberIntroduction(introduction);

            memberDetailService.save(memberDomain);

            return introduction;
        }catch(Exception e){
            return e.getMessage();
        }
    }

}
