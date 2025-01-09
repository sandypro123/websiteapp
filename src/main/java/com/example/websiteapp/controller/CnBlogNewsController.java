package com.example.websiteapp.controller;

import com.example.websiteapp.entity.CnBlogNews;
import com.example.websiteapp.service.CnBlogNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cnBlogNews")
public class CnBlogNewsController {

    private CnBlogNewsService cnBlogNewsService;
    @Autowired
    public CnBlogNewsController(CnBlogNewsService cnBlogNewsService){
        this.cnBlogNewsService=cnBlogNewsService;
    }
    @GetMapping("/getAllNews")
    public Page<CnBlogNews> getAllCnBlogNews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        log.info("getCnBlogNews");
        Pageable pageable= PageRequest.of(page,size);
        return cnBlogNewsService.getAllCnBlogNews(pageable);
    }
}
