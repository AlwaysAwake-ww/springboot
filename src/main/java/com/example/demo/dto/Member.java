package com.example.demo.dto;


import lombok.*;

@Getter
@Setter
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Member {



    private Long memberIndex;
    private String memberEmail;

    private String memberName;

    private String memberRole;

    private String joinDate;

    private String memberIntroduction;

}

