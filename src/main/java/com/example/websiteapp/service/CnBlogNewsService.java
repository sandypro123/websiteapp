package com.example.websiteapp.service;

import com.example.websiteapp.dao.CnBlogNewsDao;
import com.example.websiteapp.entity.CnBlogNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class CnBlogNewsService {
    @Autowired
    private CnBlogNewsDao cnBlogNewsDao;
    public Page<CnBlogNews> getAllCnBlogNews(Pageable pageable){
        return cnBlogNewsDao.findAll(pageable);
    }
    public void insertOrUpdate(CnBlogNews cnBlogNews){
        cnBlogNewsDao.save(cnBlogNews);
    }
    public int countByTitle(String title){
        List<CnBlogNews> cnBlogNewsList=cnBlogNewsDao.findAllByTitle(title);
        if(cnBlogNewsList==null){
            return 0;
        }
        return cnBlogNewsList.size();
    }
}
