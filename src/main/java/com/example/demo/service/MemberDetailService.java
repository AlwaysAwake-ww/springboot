package com.example.demo.service;

import com.example.demo.domain.MemberDomain;
import com.example.demo.dto.Member;
import com.example.demo.dto.MemberDetail;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // loadUserByUsername
    // username을 가지고 사용자 정보를 조회
    // 사용자의 Role과 권한(Privilege)을 조회하여, SimpleGrantedAuthority 목록을 authorities에 세팅한다.
    // Authentication 내부 principal 객체에 UserDetails 객체가 저장된다.
    // Authentication 내부 authorities 객체에 사용자의 Role과 권한(Privilege) 정보가 저장된다.
    // UserDetails에 authorities가 세팅되어 있어야, API별 role이나 권한 체크를 진행할 수 있다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        Optional<MemberDomain> result = memberRepository.findByMemberEmail(email);

        if(!result.isPresent()){
            throw new UsernameNotFoundException("이메일 또는 비밀번호를 확인하세요.");
        }
        MemberDomain member = result.get();
        return new MemberDetail(member);
    }

    public Member getMember(String email){
        Optional<MemberDomain> memberDomainOp = memberRepository.findByMemberEmail(email);
        MemberDomain memberDomain = memberDomainOp.get();

        Member member = Member.builder()
                .memberIndex(memberDomain.getMemberIndex())
                .memberName(memberDomain.getMemberName())
                .memberEmail(memberDomain.getMemberEmail())
                .memberRole(memberDomain.getMemberRole())
                .memberIntroduction(memberDomain.getMemberIntroduction())
                .joinDate(memberDomain.getJoinDate())
                .build();
        return member;
    }

    public MemberDomain getMemberDomain(String email){

        Optional<MemberDomain> memberDomainOp = memberRepository.findByMemberEmail(email);
        return memberDomainOp.get();
    }

    public void deleteTempImage(Long memberIndex){

        String rootPath = new File("").getAbsolutePath();
        String sep = File.separator;

        String targetDir = rootPath+sep+"src"+sep+"main"+sep+"resources"+sep+"static"+sep+"uploadresult"+sep+"temp"+sep+memberIndex;



        File dir = new File(targetDir);
        if(dir.exists()) {

            File[] fileList =  dir.listFiles();
            if(fileList!=null){
                for(File file: fileList){
                    file.delete();
                }
            }
            dir.delete();
        }
    }


    public List<MemberDomain> findByKeyword(String category,String keyword){

        if(category.matches("searchName")){
            System.out.println("searchName keyword");
            return memberRepository.findByMemberNameContainingOrderByMemberName(keyword).get();
        }
        else if(category.matches("searchEmail")){
            System.out.println("searchEmail keyword");
            return memberRepository.findByMemberEmailContainingOrderByMemberName(keyword).get();
        }

        return null;
    }

    public void save(MemberDomain memberDomain){

        memberRepository.save(memberDomain);
    }
}
