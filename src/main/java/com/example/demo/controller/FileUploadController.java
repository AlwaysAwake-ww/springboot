package com.example.demo.controller;


import com.example.demo.domain.ImageDomain;
import com.example.demo.domain.MemberDomain;
import com.example.demo.domain.PostDomain;
import com.example.demo.dto.Image;
import com.example.demo.dto.Member;
import com.example.demo.dto.MemberDetail;
import com.example.demo.service.ImageService;
import com.example.demo.service.MemberDetailService;
import com.example.demo.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.io.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class FileUploadController {


    private final MemberDetailService memberDetailService;
    private final PostService postService;
    private final ImageService imageService;

    @ResponseBody
    @PostMapping("/user/write/tempimgupload")
    public Image imgUpload(@RequestParam("image") MultipartFile file, @AuthenticationPrincipal MemberDetail memberDetail) throws Exception{

        MemberDomain memberDomain = memberDetailService.getMemberDomain(memberDetail.getUsername());

        Long memberIndex = memberDomain.getMemberIndex();

        System.out.println("## image upload ajax method called ##");
        String rootPath = new File("").getAbsolutePath();
        String sep = File.separator;
        String directory =rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"temp"+sep+memberIndex.toString();


        String originName = file.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originName);
        String newName = UUID.randomUUID()+"."+ext;
        System.out.println("## originName :: "+originName);
        File dir = new File(directory);

        if(!dir.exists()) {
            try {
                dir.mkdirs();
                System.out.println("directory init");
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        File newFile = new File(directory+sep+newName);
        file.transferTo(newFile);

        Image image = Image.builder()
                .originName(originName)
                .newName(newName)
                .build();

        return image;
    }

    @ResponseBody
    @PostMapping("/user/write/tempimgdelete")
    public String imgDelete(@RequestParam("imageName") String imageName, @AuthenticationPrincipal MemberDetail memberDetail) throws Exception{

        MemberDomain memberDomain = memberDetailService.getMemberDomain(memberDetail.getUsername());

        Long memberIndex = memberDomain.getMemberIndex();


        String rootPath = new File("").getAbsolutePath();
        String sep = File.separator;
        String directory =rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"temp"+sep+memberIndex.toString();

        System.out.println("## imageName :: "+imageName);
        Path filePath = Paths.get(directory +sep+ imageName);
        Files.delete(filePath);

        return imageName;
    }

    @ResponseBody
    @PostMapping("/user/write/thumbnailupload")
    public Image thumbnailUpload(@RequestParam("image") MultipartFile file, @AuthenticationPrincipal MemberDetail memberDetail) throws Exception{

        MemberDomain memberDomain = memberDetailService.getMemberDomain(memberDetail.getUsername());

        Long memberIndex = memberDomain.getMemberIndex();

        String rootPath = new File("").getAbsolutePath();
        String sep = File.separator;
        String directory =rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"temp"+sep+memberIndex.toString();


        String originName = file.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originName);
        String newName = "thumbnail-"+UUID.randomUUID()+"."+ext;

        File dir = new File(directory);

        if(!dir.exists()) {
            try {
                dir.mkdirs();
                System.out.println("directory init");
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        if(dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            for (File existFile : files) {
                System.out.println("## exist file :: "+existFile.getName());

                if (existFile.isFile() && existFile.getName().startsWith("thumbnail-")) {
                    System.out.println("## exist file checked :: "+existFile.getName());
                    existFile.delete();
                }
            }
        }

        File newFile = new File(directory+sep+newName);
        file.transferTo(newFile);

        Image image = Image.builder()
                .originName(originName)
                .newName(newName)
                .build();

        return image;
    }

    @GetMapping("/uploadresult/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        String sep = File.separator;
        Path imagePath = Paths.get("src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"images"+sep+imageName);
        Resource resource = new UrlResource(imagePath.toUri().toURL());

        MediaType mediaType;
        String extension = FilenameUtils.getExtension(imageName);

        if (extension.equalsIgnoreCase("png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (extension.equalsIgnoreCase("gif")) {
            mediaType = MediaType.IMAGE_GIF;
        } else {
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok().contentLength(resource.contentLength())
                .contentType(mediaType)
                .body(new InputStreamResource(resource.getInputStream()));
    }


    @GetMapping("/uploadresult/images/thumbnail/{postIndex}")
    public ResponseEntity<Resource> getThumbnail(@PathVariable("postIndex") Long postIndex) throws IOException {



        PostDomain postDomain = postService.findByIndex(postIndex);
        ImageDomain imageDomain = postService.getThumbnail(postIndex);

        String sep = File.separator;

        Resource resource;
        String extension="";


        // DB에 파일이 없을 때
        if(imageDomain!=null){

            Path imagePath = Paths.get("src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"images"+sep+postDomain.getMemberDomain().getMemberIndex().toString()+sep+postIndex.toString()+sep+imageDomain.getNewName());
            resource = new UrlResource(imagePath.toUri().toURL());
            extension = FilenameUtils.getExtension(imageDomain.getNewName());
        }

        else{
            Path imagePath = Paths.get("src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"default"+sep+"noimage.jpg");
            resource = new UrlResource(imagePath.toUri().toURL());
            extension = FilenameUtils.getExtension("defaultThumbnail.jpg");

        }

        MediaType mediaType;

        if (extension.equalsIgnoreCase("png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (extension.equalsIgnoreCase("gif")) {
            mediaType = MediaType.IMAGE_GIF;
        } else {
            mediaType = MediaType.IMAGE_JPEG;
        }

        // 실제 파일이 없을 때
        if(!resource.exists()){
            Path imagePath = Paths.get("src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"default"+sep+"noimage.jpg");
            Resource defaultResource = new UrlResource(imagePath.toUri().toURL());


            return ResponseEntity.ok().contentLength(defaultResource.contentLength())
                    .contentType(mediaType)
                    .body(new InputStreamResource(defaultResource.getInputStream()));
        }


        // 실제 파일이 있을 때
        return ResponseEntity.ok().contentLength(resource.contentLength())
                .contentType(mediaType)
                .body(new InputStreamResource(resource.getInputStream()));

    }


    @GetMapping("/uploadresult/images/{memberIndex}/{postIndex}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("memberIndex") Long memberIndex ,@PathVariable("postIndex") Long postIndex, @PathVariable("imageName") String imageName) throws IOException {



        PostDomain postDomain = postService.findByIndex(postIndex);
        ImageDomain imageDomain = imageService.findByPostDomainAndNewName(postDomain, imageName);


        String sep = File.separator;

        Resource resource;
        String extension="";

        if(imageDomain!=null){

            Path imagePath = Paths.get("src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"images"+sep+postDomain.getMemberDomain().getMemberIndex().toString()+sep+postIndex.toString()+sep+imageDomain.getNewName());
            resource = new UrlResource(imagePath.toUri().toURL());
            extension = FilenameUtils.getExtension(imageDomain.getNewName());
        }

        else{
            Path imagePath = Paths.get("src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"default"+sep+"noImage.jpg");
            resource = new UrlResource(imagePath.toUri().toURL());
            extension = FilenameUtils.getExtension("defaultThumbnail.jpg");

        }



        MediaType mediaType;

        if (extension.equalsIgnoreCase("png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (extension.equalsIgnoreCase("gif")) {
            mediaType = MediaType.IMAGE_GIF;
        } else {
            mediaType = MediaType.IMAGE_JPEG;
        }

        // 실제 파일이 없을 때
        if(!resource.exists()){
            Path imagePath = Paths.get("src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"default"+sep+"noimage.jpg");
            Resource defaultResource = new UrlResource(imagePath.toUri().toURL());


            return ResponseEntity.ok().contentLength(defaultResource.contentLength())
                    .contentType(mediaType)
                    .body(new InputStreamResource(defaultResource.getInputStream()));
        }

        return ResponseEntity.ok().contentLength(resource.contentLength())
                .contentType(mediaType)
                .body(new InputStreamResource(resource.getInputStream()));

    }



}
