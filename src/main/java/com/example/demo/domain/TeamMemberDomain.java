package com.example.demo.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Table(name="TEAM_MEMBER")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "TEAM_MEMBER_SEQ_GENERATOR",
        sequenceName = "TEAM_MEMBER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class TeamMemberDomain {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "TEAM_MEMBER_SEQ_GENERATOR"
    )
    @Column(name="team_member_index")
    private Long teamMemberIndex;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="team_index")
    private TeamDomain teamDomain;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_index")
    private MemberDomain memberDomain;



}
