package com.example.demo.dto;


import com.example.demo.domain.MemberDomain;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


@RequiredArgsConstructor
@AllArgsConstructor
public class MemberDetail implements UserDetails {


    private MemberDomain memberDomain;


    // 사용자에게 부여된 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collect = new ArrayList<>();

        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return memberDomain.getMemberRole();
            }
        });

        return collect;
    }

    public MemberDomain getMemberDomain(){

        return this.memberDomain;
    }

    public String getName(){

        return memberDomain.getMemberName();
    }

    public String getRole(){
        return memberDomain.getMemberRole();
    }

    // 비밀번호 가져오기
    @Override
    public String getPassword() {
        return memberDomain.getMemberPassword();
    }

    // 유저 이름 가져오기
    @Override
    public String getUsername() {
        return memberDomain.getMemberEmail();
    }


    // 계정 만료
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 credential
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 비활성화 (휴면계정 등)
    @Override
    public boolean isEnabled() {
        return true;
    }
}

