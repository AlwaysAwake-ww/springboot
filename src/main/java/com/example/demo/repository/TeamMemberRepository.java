package com.example.demo.repository;

import com.example.demo.domain.MemberDomain;
import com.example.demo.domain.PostDomain;
import com.example.demo.domain.TeamDomain;
import com.example.demo.domain.TeamMemberDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMemberDomain, Long> {


    Optional<List<TeamMemberDomain>> findByMemberDomain(MemberDomain memberDomain);

    Optional<TeamMemberDomain> findByMemberDomainAndTeamDomain(MemberDomain memberDomain, TeamDomain teamDomain);

    Optional<List<TeamMemberDomain>> findByMemberDomain_MemberIndexAndTeamDomain_TeamNameContaining(Long memberIndex, String teamName);

    Optional<List<TeamMemberDomain>> findByTeamDomain_TeamIndex(Long teamIndex);


    void deleteAllByTeamDomain_TeamIndex(Long teamIndex);



}
