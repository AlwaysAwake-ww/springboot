package com.example.demo.repository;

import com.example.demo.domain.MemberDomain;
import com.example.demo.domain.TeamDomain;
import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamDomain, Long> {


    Optional<TeamDomain> findByTeamNameContaining(String name);

    Optional<TeamDomain> findByTeamName(String name);


}
