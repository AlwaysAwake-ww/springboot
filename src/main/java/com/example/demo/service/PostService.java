package com.example.demo.service;

import com.example.demo.controller.ImageHandler;
import com.example.demo.domain.ImageDomain;
import com.example.demo.domain.MemberDomain;
import com.example.demo.domain.PostDomain;
import com.example.demo.domain.TeamDomain;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.PostPageableRepository;
import com.example.demo.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostPageableRepository postPageableRepository;
    private final ImageRepository imageRepository;

    public void save(PostDomain postDomain){
        postRepository.save(postDomain);
    }



    public void delete(PostDomain postDomain){

        List<ImageDomain> imageDomainList = imageRepository.findByPostDomain(postDomain).get();


        String postIndex = postDomain.getPostIndex().toString();
        String memberIndex = postDomain.getMemberDomain().getMemberIndex().toString();
        String rootPath = new File("").getAbsolutePath();
        String sep = File.separator;
        String directory = rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"images"+sep+memberIndex+sep+postIndex;

        ImageHandler imageHandler = ImageHandler.builder()
                .imageDomainList(imageDomainList)
                .realDirectory(directory)
                .build();

        imageHandler.deleteImage();

        postRepository.delete(postDomain);
    }


    @Transactional
    public void saveWithImage(PostDomain postDomain, List<String> originName, List<String> newName){

        System.out.println("## saveWithImage called");
        PostDomain savedPost = postRepository.save(postDomain);

        int size = originName.size();


        String rootPath = new File("").getAbsolutePath();
        String sep = File.separator;

        String postIndex = savedPost.getPostIndex().toString();
        String memberIndex = savedPost.getMemberDomain().getMemberIndex().toString();

        String tempDirectory =rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"temp"+sep+memberIndex;
        String newDirectory = rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"images"+sep+memberIndex+sep+postIndex;


        List<ImageDomain> imageDomainList = new ArrayList<>();

        for(int i=0; i<size; i++){
            ImageDomain imageDomain = ImageDomain.builder()
                    .originName(originName.get(i))
                    .newName(newName.get(i))
                    .imagePath(newDirectory)
                    .imageExt(FilenameUtils.getExtension(originName.get(i)))
                    .postDomain(savedPost)
                    .build();

            imageDomainList.add(imageDomain);
        }

        ImageHandler imageHandler = ImageHandler.builder()
                .imageDomainList(imageDomainList)
                .tempDirectory(tempDirectory)
                .realDirectory(newDirectory)
                .build();

        imageHandler.deleteImage();
        imageHandler.saveRealPath();


        imageRepository.deleteAllByPostDomain(postDomain);

        for(ImageDomain image : imageDomainList){
            imageRepository.save(image);
        }
    }


    public PostDomain findByIndex(Long postNum){

        return postRepository.findById(postNum).get();
    }

    public Page<PostDomain> findMyPosts(MemberDomain memberDomain, Pageable pageable){

        return postPageableRepository.findByMemberDomain(memberDomain, pageable);
    }

    public Page<PostDomain> findAll(Pageable pageable){

        return postPageableRepository.findAll(pageable);
    }

    public Page<PostDomain> search(String category, String keyword, Pageable pageable ){


        if(category.equals("name")){
            return postPageableRepository.findByMemberDomain_MemberNameContainingOrderByPostDateDesc(keyword, pageable);
        }
        else if(category.equals("email")){
            return postPageableRepository.findByMemberDomain_MemberEmailContainingOrderByPostDateDesc(keyword, pageable);
        }

        else if(category.equals("title")){

            return postPageableRepository.findByPostTitleContainingOrderByPostDateDesc(keyword, pageable);
        }

        else{
            return null;
        }
    }

    public Page<PostDomain> searchMine(MemberDomain memberDomain,String category, String keyword, Pageable pageable ){


        if(category.equals("name")){
            return postPageableRepository.findByMemberDomainAndMemberDomain_MemberNameContainingOrderByPostDateDesc(memberDomain, keyword, pageable);
        }
        else if(category.equals("email")){
            return postPageableRepository.findByMemberDomainAndMemberDomain_MemberEmailContainingOrderByPostDateDesc(memberDomain, keyword, pageable);
        }

        else if(category.equals("title")){

            return postPageableRepository.findByMemberDomainAndPostTitleContainingOrderByPostDateDesc(memberDomain, keyword, pageable);
        }

        else{
            return null;
        }
    }

    public List<PostDomain> getTeamPost(TeamDomain teamDomain){


        return postRepository.findByTeamDomain(teamDomain).get();
    }
    public ImageDomain getThumbnail(Long postIndex){

        PostDomain postDomain = postRepository.findByPostIndex(postIndex).get();
        Optional<List<ImageDomain>> optional = imageRepository.findByPostDomain(postDomain);
        List<ImageDomain> imageDomainList = optional.get();


        for(ImageDomain image : imageDomainList){

            if(image.getNewName().startsWith("thumbnail-")){
                return image;
            }
        }
            return null;
    }

    public void copyToTemp(PostDomain postDomain){

        String rootPath = new File("").getAbsolutePath();
        String sep = File.separator;

        String postIndex = postDomain.getPostIndex().toString();
        String memberIndex = postDomain.getMemberDomain().getMemberIndex().toString();


        String tempDirectory =rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"temp"+sep+memberIndex;
        String originDirectory = rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"images"+sep+memberIndex+sep+postIndex;

        List<ImageDomain> imageDomainList = imageRepository.findByPostDomain(postDomain).get();

        ImageHandler imageHandler = ImageHandler.builder()
                .imageDomainList(imageDomainList)
                .tempDirectory(tempDirectory)
                .realDirectory(originDirectory)
                .build();

        imageHandler.copyTempPath();
    }
}
