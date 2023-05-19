package com.example.demo.controller;


import com.example.demo.domain.ImageDomain;
import com.example.demo.domain.MemberDomain;
import com.example.demo.domain.PostDomain;
import com.example.demo.domain.TeamDomain;
import com.example.demo.dto.Image;
import com.example.demo.dto.Member;
import com.example.demo.dto.MemberDetail;
import com.example.demo.repository.TeamRepository;
import com.example.demo.service.ImageService;
import com.example.demo.service.MemberDetailService;
import com.example.demo.service.PostService;
import com.example.demo.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final MemberDetailService memberDetailService;
    private final PostService postService;
    private final ImageService imageService;
    private final TeamService teamService;



    @GetMapping("/user/write")
    public ModelAndView write(@AuthenticationPrincipal MemberDetail memberDetail, ModelAndView model){

        model.addObject("member",memberDetailService.getMember(memberDetail.getUsername()));
        model.setViewName("write");
        return model;
    }

    @ResponseBody
    @PostMapping("/user/write/submit")
    public String posting(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "imageOriginNameArr[]", required = false) List<String> originNameList, @RequestParam(value = "imageNewNameArr[]",required = false) List<String> newNameList, @RequestParam(value = "teamName", required = false)String teamName, @AuthenticationPrincipal MemberDetail memberDetail){

        MemberDomain memberDomain = memberDetailService.getMemberDomain(memberDetail.getUsername());
        System.out.println("### team name :: "+teamName);

        TeamDomain teamDomain = teamService.getTeamByName(teamName);


            PostDomain postDomain = PostDomain.builder()
                    .postTitle(title)
                    .postContent(content)
                    .memberDomain(memberDomain)
                    .teamDomain(teamDomain)
                    .postDate(LocalDateTime.now().toLocalDate().toString())
                    .build();


        try{
            if(originNameList==null || originNameList.isEmpty())
                postService.save(postDomain);

            else
                postService.saveWithImage(postDomain, originNameList, newNameList);

            return "success";

        }
        catch(Exception e){
            e.getStackTrace();
            return "fail :: "+e.getMessage();
        }
    }


    @GetMapping("/user/allpost")
    public ModelAndView allpost(@PageableDefault(size=6, page=0, sort="postIndex", direction = Sort.Direction.DESC) Pageable pageable, ModelAndView model, @RequestParam(value = "keyword", required = false)String keyword, @RequestParam(value = "category", required = false)String category, @AuthenticationPrincipal MemberDetail memberDetail){

        Member member = memberDetailService.getMember(memberDetail.getUsername());


        System.out.println("## keyword :: "+keyword);
        System.out.println("## category :: "+category);

        Page<PostDomain> list;

        if(keyword==null||category==null){
             list = postService.findAll(pageable);
        }
        else{

            list = postService.search(category, keyword, pageable);
        }



        int curPage;
        if (list.isEmpty()) {
            curPage = 0;
        } else {
            curPage = list.getPageable().getPageNumber() + 1;
        }
        int startPage = Math.max(curPage - 4, 1);
        int endPage = Math.min(curPage+9, list.getTotalPages());


        model.addObject("list", list);
        model.addObject("curPage", curPage);
        model.addObject("startPage", startPage);
        model.addObject("endPage", endPage);

        model.addObject("member",member);
        model.setViewName("allpost");
        return model;
    }

    @GetMapping("/user/mypost")
    public ModelAndView mypost(@PageableDefault(size=6, page=0, sort="postIndex", direction = Sort.Direction.DESC) Pageable pageable, ModelAndView model,@RequestParam(value = "keyword", required = false)String keyword, @RequestParam(value = "category", required = false)String category, @AuthenticationPrincipal MemberDetail memberDetail){


        Member member = memberDetailService.getMember(memberDetail.getUsername());
//        Page<PostDomain> list = postService.findMyPosts(memberDetailService.getMemberDomain(memberDetail.getUsername()),pageable);

        Page<PostDomain> list;



        if(keyword==null||category==null){
            list = postService.findMyPosts(memberDetailService.getMemberDomain(memberDetail.getUsername()),pageable);
        }
        else{

            list = postService.searchMine(memberDetailService.getMemberDomain(memberDetail.getUsername()), category, keyword, pageable);
        }

        int curPage;
        if (list.isEmpty()) {
            curPage = 0;
        } else {
            curPage = list.getPageable().getPageNumber() + 1;
        }


        int startPage = Math.max(curPage - 4, 1);
        int endPage = Math.min(curPage+9, list.getTotalPages());




        model.addObject("list", list);
        model.addObject("curPage", curPage);
        model.addObject("startPage", startPage);
        model.addObject("endPage", endPage);
        model.addObject("member",member);
        model.setViewName("mypost");
        return model;
    }

    @GetMapping("/user/post/{postNum}")
    public ModelAndView postDetail(@PathVariable("postNum") Long postNum, ModelAndView model, @AuthenticationPrincipal MemberDetail memberDetail){



        model.addObject("member",memberDetailService.getMember(memberDetail.getUsername()));
        PostDomain postDomain = postService.findByIndex(postNum);
        model.addObject("post", postDomain);
        model.setViewName("content");
        return model;
    }


    @PostMapping("/user/post/delete/{postNum}")
    public ModelAndView postDelete(@PathVariable("postNum") Long postNum, @AuthenticationPrincipal MemberDetail memberDetail){

        ModelAndView model = new ModelAndView();
        model.addObject("member", memberDetailService.getMember(memberDetail.getUsername()));
        try{

            postService.delete(postService.findByIndex(postNum));
            model.setViewName("redirect:/user/mypost");
            return model;

        }catch(Exception e) {
            throw new RuntimeException("삭제 중 에러 발생");
        }
    }


    @GetMapping("/user/post/edit/{postNum}")
    public ModelAndView postEdit(@PathVariable("postNum") Long postNum,@AuthenticationPrincipal MemberDetail memberDetail, ModelAndView model){

        System.out.println(" ## edit method called");

        PostDomain postDomain = postService.findByIndex(postNum);
        List<ImageDomain> imageDomainList = imageService.findByPostDomain(postDomain);

        postService.copyToTemp(postDomain);


        for(ImageDomain image : imageDomainList){
            if(image.getNewName().startsWith("thumbnail-")){
                System.out.println("## thumbnail exist :: "+image.getNewName());
                model.addObject("thumbnail", image.getNewName());
            }
        }

        if(postDomain.getTeamDomain()!=null)
            model.addObject("teamName", postDomain.getTeamDomain().getTeamName());

        model.addObject("member",memberDetailService.getMember(memberDetail.getUsername()));
        model.addObject("postDomain", postDomain);
        model.addObject("imageList", imageDomainList);
        model.setViewName("contentEdit");
        return model;
    }

    @ResponseBody
    @PostMapping("/user/post/edit/{postNum}")
    public String editSave(@PathVariable("postNum")Long postNum ,@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "imageOriginNameArr[]", required = false) List<String> originNameList, @RequestParam(value = "imageNewNameArr[]",required = false) List<String> newNameList, @RequestParam(value = "teamName", required = false)String teamName, @AuthenticationPrincipal MemberDetail memberDetail){


        MemberDomain memberDomain = memberDetailService.getMemberDomain(memberDetail.getUsername());

        TeamDomain teamDomain = teamService.getTeamByName(teamName);


        PostDomain postDomain = PostDomain.builder()
                .postIndex(postNum)
                .postTitle(title)
                .postContent(content)
                .postDate(LocalDateTime.now().toLocalDate().toString())
                .teamDomain(teamDomain)
                .memberDomain(memberDomain)
                .build();



        try{
            if(originNameList==null || originNameList.isEmpty())
                postService.save(postDomain);

            else{
                postService.saveWithImage(postDomain, originNameList, newNameList);
            }


            return "success";

        }
        catch(Exception e){
            e.getStackTrace();
            return "fail :: "+e.getMessage();
        }
    }



    @GetMapping("/post/{postNum}")
    public ModelAndView tempLink(){

        ModelAndView model = new ModelAndView();

        return model;
    }


}
