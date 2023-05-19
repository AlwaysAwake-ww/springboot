package com.example.demo.service;


import com.example.demo.domain.MemberDomain;
import com.example.demo.dto.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final MemberRepository memberRepository;
    public void save(MemberDomain memberDomain){
        memberRepository.save(memberDomain);
    }
}