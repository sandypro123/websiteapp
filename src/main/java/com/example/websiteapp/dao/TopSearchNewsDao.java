package com.example.websiteapp.dao;

import com.example.websiteapp.entity.TopSearchNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: sandy
 * \* Date: 2024/11/12
 * \* Time: 17:45
 * \* Description:
 * \
 */
public interface TopSearchNewsDao extends JpaRepository<TopSearchNews,Integer> {
    List<TopSearchNews> findAll();
}