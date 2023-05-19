package com.example.demo.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="TEAM")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "TEAM_SEQ_GENERATOR",
        sequenceName = "TEAM_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class TeamDomain {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "TEAM_SEQ_GENERATOR"
    )
    @Column(name="team_index")
    private Long teamIndex;

    @Column(name="team_name")
    private String teamName;


}
