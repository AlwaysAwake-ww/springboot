package com.example.demo.repository;

import com.example.demo.domain.MemberDomain;
import com.example.demo.domain.PostDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostPageableRepository extends PagingAndSortingRepository<PostDomain, Long> {

    Optional<PostDomain> findByPostIndex(Long id);

    Page<PostDomain> findByMemberDomain(MemberDomain memberDomain, Pageable pageable);
    Page<PostDomain> findAll(Pageable pageable);


//    모든 포스트 검색
    Page<PostDomain> findByPostTitleContainingOrderByPostDateDesc(String title, Pageable pageable);

    Page<PostDomain> findByMemberDomain_MemberEmailContainingOrderByPostDateDesc(String email, Pageable pageable);

    Page<PostDomain> findByMemberDomain_MemberNameContainingOrderByPostDateDesc(String email, Pageable pageable);


//     내 포스트 검색
    Page<PostDomain> findByMemberDomainAndPostTitleContainingOrderByPostDateDesc(MemberDomain memberDomain,String title, Pageable pageable);

    Page<PostDomain> findByMemberDomainAndMemberDomain_MemberEmailContainingOrderByPostDateDesc(MemberDomain memberDomain, String email, Pageable pageable);

    Page<PostDomain> findByMemberDomainAndMemberDomain_MemberNameContainingOrderByPostDateDesc(MemberDomain memberDomain,String name, Pageable pageable);


}
