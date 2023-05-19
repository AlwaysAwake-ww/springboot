package com.example.demo.controller;


import com.example.demo.domain.MemberDomain;
import com.example.demo.domain.PostDomain;
import com.example.demo.domain.TeamDomain;
import com.example.demo.domain.TeamMemberDomain;
import com.example.demo.dto.MemberDetail;
import com.example.demo.service.MemberDetailService;
import com.example.demo.service.PostService;
import com.example.demo.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamController {


    private final MemberDetailService memberDetailService;
    private final TeamService teamService;
    private final PostService postService;

    @GetMapping("/user/team")
    public ModelAndView team(@AuthenticationPrincipal MemberDetail memberDetail){

        ModelAndView model = new ModelAndView("teamManagement");

        List<TeamDomain> teamDomainList = teamService.getTeamList(memberDetailService.getMemberDomain(memberDetail.getUsername()));
        System.out.println("## "+teamDomainList);
        model.addObject("teamDomainList", teamDomainList);

        return model;
    }


    @GetMapping("/user/team/create")
    public ModelAndView teamCreate(){

        return new ModelAndView("teamCreate");
    }

    @ResponseBody
    @PostMapping("/user/team/search")
    public List<String> teamSearch(@RequestParam("memberIndex") Long memberIndex,@RequestParam("teamName") String keyword){


            List<TeamDomain> teamDomainList= teamService.getByMemberIndexAndTeamName(memberIndex, keyword);
            List<String> teamNameList = new ArrayList<>();


            for(TeamDomain teamDomain : teamDomainList){
                System.out.println("# "+teamDomain.getTeamName());
                teamNameList.add(teamDomain.getTeamName());
            }

            if(teamNameList.size()<1){
                List<String> error = new ArrayList<>();
                error.add("no team");
                return error;
            }
            return teamNameList;
    }

    @ResponseBody
    @PostMapping("/user/team/create/submit")
    public String teamCreate(@RequestParam(value = "teamName", required = true)String teamName,@RequestParam("memberEmailArray[]") List<String> memberEmailList){

        try{
            teamService.create(teamName, memberEmailList);
            return "success";
        }
        catch(Exception e){
            e.getStackTrace();
            return e.getMessage();
        }
    }


    @GetMapping("/user/team/info/{teamIndex}")
    public ModelAndView teamInfo(@PathVariable("teamIndex")Long teamIndex, ModelAndView model){

        List<MemberDomain> memberDomainList = teamService.getAllMember(teamIndex);

        TeamDomain teamDomain = teamService.getTeam(teamIndex);

        List<PostDomain> postDomainList = postService.getTeamPost(teamDomain);



        model.addObject("list", postDomainList);
        model.addObject("memberList", memberDomainList);
        model.addObject("team", teamDomain);
        model.setViewName("teamInfo");

        return model;
    }

    @ResponseBody
    @PostMapping("/user/team/info/{teamIndex}")
    public String teamInfoEdit(@PathVariable("teamIndex")Long teamIndex, @RequestParam(value = "teamName", required = true)String teamName,@RequestParam("memberEmailArray[]") List<String> memberEmailList){

        try{
            teamService.teamInfoEdit(teamIndex, teamName, memberEmailList);
            return "success";
        }
        catch(Exception e){

            return e.getMessage();
        }
    }


    @ResponseBody
    @DeleteMapping("/user/team/delete/{teamIndex}")
    public String teamDelete(@PathVariable("teamIndex") Long teamIndex){

        try{

            teamService.teamDelete(teamIndex);
            return "success";
        }catch(Exception e){

            return e.getMessage();
        }
    }
}
