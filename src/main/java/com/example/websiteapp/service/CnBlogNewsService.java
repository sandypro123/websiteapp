package com.example.websiteapp.service;

import com.example.websiteapp.dao.CnBlogNewsDao;
import com.example.websiteapp.entity.CnBlogNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class CnBlogNewsService {
    @Autowired
    private CnBlogNewsDao cnBlogNewsDao;
    public Page<CnBlogNews> getAllCnBlogNews(Pageable pageable){
        return cnBlogNewsDao.findAll(pageable);
    }
}
