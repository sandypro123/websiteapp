package com.example.websiteapp.controller;

import com.example.websiteapp.service.TopSearchNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api/topSearchNews")
public class TopSearchNewsController{
    private TopSearchNewsService topSearchNewsService;
    @Autowired
    public TopSearchNewsController(TopSearchNewsService topSearchNewsService){
        this.topSearchNewsService=topSearchNewsService;
    }
    @GetMapping("/getAllNews")
    public ResponseEntity<Object> getAllNews(){
        List<Map<String, List>> newsList=topSearchNewsService.getAllTopSearchNews();
        Map<String,Object> response=new HashMap<>();
        response.put("status","ok");
        response.put("data",newsList);
        return ResponseEntity.ok(response);
    }
}
