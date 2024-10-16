package com.example.websiteapp.controller;


import com.example.websiteapp.service.CnBlogNewsService;
import com.example.websiteapp.task.CnBlogNewsTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/cnblogSpider")
public class CnBlogNewsController extends BaseController {
    @Value("${cnBlog.welfareUrl}")
    private String welfareUrl;
    @Value("${cnBlog.spiderTaskName}")
    private String cnBlogNewsSpiderTaskName;

    @Autowired
    private CnBlogNewsService cnBlogNewsService;
    @Autowired
    private CnBlogNewsTask cnBlogSpiderTask;


    @RequestMapping("/newsSpider")
    public DeferredResult newsSpider(){
        DeferredResult deferredResult=new DeferredResult();
        ThreadFactory namedThreadFactory=new ThreadFactoryBuilder().setNameFormat("cnblognewsSpider-pool-%d").build();
        ExecutorService singleThreadPool=new ThreadPoolExecutor(1,1,0L, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>(1024),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());
        cnBlogSpiderTask.setCnBlogUrl(welfareUrl);
        singleThreadPool.execute(cnBlogSpiderTask);
        singleThreadPool.shutdown();
        sealSuccess(deferredResult,"success! spider on going!");
        return deferredResult;
    }
    /*@CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("/api/v1/data")
    public Page<CnBlogNews> getAllCnBlogNews(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size){
        Pageable pageable= PageRequest.of(page,size);
        return cnBlogNewsService.getAllCnBlogNews(pageable);
    }*/
}
