package com.example.websiteapp.dao;

import com.example.websiteapp.entity.CnBlogNews;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CnBlogNewsDao extends JpaRepository<CnBlogNews,Integer> {
    Page<CnBlogNews> findAll(Pageable pageable);

    List<CnBlogNews> findallByTitle(String title);
}
