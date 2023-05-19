package com.example.demo.service;


import com.example.demo.domain.MemberDomain;
import com.example.demo.domain.TeamDomain;
import com.example.demo.domain.TeamMemberDomain;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.TeamMemberRepository;
import com.example.demo.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {



    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;


    public TeamDomain getTeam(Long id){


        return teamRepository.findById(id).get();
    }

    public TeamDomain getTeamByName(String teamName){

        TeamDomain teamDomain = teamRepository.findByTeamName(teamName).get();
        System.out.println("### team service getTeamByTeamName :: "+teamDomain);

        return teamDomain;
    }

    @Transactional
    public List<TeamDomain> getTeamList(MemberDomain memberDomain){


        List<TeamMemberDomain> teamMemberDomainList= teamMemberRepository.findByMemberDomain(memberDomain).get();

        List<TeamDomain> teamDomainList = new ArrayList<>();

        for(TeamMemberDomain teamMemberDomain:teamMemberDomainList){
            teamMemberDomain.getTeamDomain().getTeamIndex();

            teamDomainList.add(teamRepository.findById(teamMemberDomain.getTeamDomain().getTeamIndex()).get());
        }

        return teamDomainList;
    }



    public void create(String teamName,List<String> memberEmailList){

        TeamDomain teamDomain = TeamDomain.builder()
                .teamName(teamName)
                .build();


        teamRepository.save(teamDomain);

        for(String s : memberEmailList){
            MemberDomain memberDomain = memberRepository.findByMemberEmail(s).get();

            TeamMemberDomain teamMemberDomain = TeamMemberDomain.builder()
                    .teamDomain(teamDomain)
                    .memberDomain(memberDomain)
                    .build();
            teamMemberRepository.save(teamMemberDomain);
        }
    }

    public List<TeamDomain> getByMemberIndexAndTeamName(Long memberIndex, String keyword){

        List<TeamMemberDomain> teamMemberDomainList = teamMemberRepository.findByMemberDomain_MemberIndexAndTeamDomain_TeamNameContaining(memberIndex, keyword).get();

        if(teamMemberDomainList!=null){

            List<TeamDomain> teamDomainList = new ArrayList<>();
            for(TeamMemberDomain teamMemberDomain : teamMemberDomainList){


                Long teamIndex = teamMemberDomain.getTeamDomain().getTeamIndex();
                TeamDomain teamDomain = teamRepository.findById(teamIndex).get();

                teamDomainList.add(teamDomain);

            }
            return teamDomainList;
        }
        else{
            return null;
        }
    }

    public List<MemberDomain> getAllMember(Long teamIndex){

        List<TeamMemberDomain> teamMemberDomainList = teamMemberRepository.findByTeamDomain_TeamIndex(teamIndex).get();

        List<MemberDomain> memberDomainList = new ArrayList<>();
        for(TeamMemberDomain teamMemberDomain: teamMemberDomainList){

            memberDomainList.add(memberRepository.findById(teamMemberDomain.getMemberDomain().getMemberIndex()).get());
        }

        return memberDomainList;
    }


    @Transactional
    public void teamInfoEdit(Long teamIndex, String teamName,List<String> memberEmailList){


        TeamDomain teamDomain = TeamDomain.builder()
                .teamIndex(teamIndex)
                .teamName(teamName)
                .build();

        System.out.println("#####");
        System.out.println(teamDomain.getTeamIndex());
        System.out.println(teamDomain.getTeamName());


        teamRepository.save(teamDomain);
        teamMemberRepository.deleteAllByTeamDomain_TeamIndex(teamIndex);

        for(String email : memberEmailList){
            MemberDomain memberDomain = memberRepository.findByMemberEmail(email).get();

            TeamMemberDomain teamMemberDomain = TeamMemberDomain.builder()
                    .teamDomain(teamDomain)
                    .memberDomain(memberDomain)
                    .build();
            teamMemberRepository.save(teamMemberDomain);

        }
    }


    public void teamDelete(Long teamIndex){

        teamRepository.deleteById(teamIndex);
    }

}