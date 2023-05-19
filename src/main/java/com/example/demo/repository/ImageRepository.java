package com.example.demo.repository;

import com.example.demo.domain.ImageDomain;
import com.example.demo.domain.PostDomain;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageDomain, Long> {


    @Transactional
    ImageDomain save(ImageDomain imageDomain);

    @Transactional
    Optional<List<ImageDomain>> findByPostDomain(PostDomain postDomain);

    @Transactional
    Optional<ImageDomain> findByPostDomainAndNewName(PostDomain postDomain,String imageName);

    @Transactional
    void deleteAllByPostDomain(PostDomain postDomain);
}
