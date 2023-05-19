package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="POST")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "POST_SEQ_GENERATOR",
        sequenceName = "POST_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class PostDomain {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "POST_SEQ_GENERATOR"
    )    @Column(name="post_index")
    private Long postIndex;

    @Column(nullable = false, name="post_title")
    private String postTitle;


    @Column(nullable = false, name="post_content")
    private String postContent;

    @Column(nullable = true, name="post_temp_url")
    private String postTempLink;

    @Column(nullable = true, name="post_date")
    private String postDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_index")
    private MemberDomain memberDomain;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="team_index", nullable = true)
    private TeamDomain teamDomain;

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="team_index")
//    private TeamDomain teamDomain;


}



