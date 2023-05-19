package com.example.demo.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="MEMBER")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class MemberDomain {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR"
    )
    @Column(name="member_index")
    private Long memberIndex;

    @Column(nullable = false, name="member_email")
    private String memberEmail;

    @Column(nullable = false, name="member_password")
    private String memberPassword;

    @Column(nullable = false, name="member_name")
    private String memberName;


    @Column(nullable = true, name="memberIntroduction")
    private String memberIntroduction;


    @Column(nullable = false, name="member_role")
    private String memberRole;

    @Column(nullable = false, name="join_date")
    private String joinDate;


    public String encrypt(){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return "{bcrypt}"+encoder.encode(memberPassword);
    }
}
