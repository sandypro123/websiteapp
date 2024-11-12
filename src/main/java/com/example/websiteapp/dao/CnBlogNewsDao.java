package com.example.websiteapp.dao;

import com.example.websiteapp.entity.CnBlogNews;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CnBlogNewsDao extends JpaRepository<CnBlogNews, Integer> {

    // 使用正确的Spring Data Pageable导入
    Page<CnBlogNews> findAll(Pageable pageable);

    // 修正findAllByTitle方法的命名
    List<CnBlogNews> findAllByTitle(String title);
}
