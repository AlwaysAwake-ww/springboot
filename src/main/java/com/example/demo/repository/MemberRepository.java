package com.example.demo.repository;


import com.example.demo.domain.MemberDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberDomain, Long> {


    Optional<MemberDomain> findByMemberEmail(String email);


    Optional<List<MemberDomain>> findByMemberNameContainingOrderByMemberName(String name);

    Optional<List<MemberDomain>> findByMemberEmailContainingOrderByMemberName(String email);


}
