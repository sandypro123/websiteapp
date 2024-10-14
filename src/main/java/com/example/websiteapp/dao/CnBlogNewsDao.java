package com.example.websiteapp.dao;

import com.example.websiteapp.entity.CnBlogNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CnBlogNewDao extends JpaRepository<CnBlogNews.CnblogNews,Integer> {
    List<CnBlogNews.CnblogNews> findAll(Pageable pageable);
}
