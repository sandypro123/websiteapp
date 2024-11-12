package com.example.websiteapp.service;

import com.example.websiteapp.dao.TopSearchNewsDao;
import com.example.websiteapp.entity.TopSearchNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: sandy
 * \* Date: 2024/11/12
 * \* Time: 17:44
 * \* Description:
 * \
 */
@Service
public class TopSearchNewsService {
    @Autowired
    private TopSearchNewsDao topSearchNewsDao;
    public List<Map<String, List>> getAllTopSearchNews(){
        // 直接获取所有热搜新闻，然后根据平台分类
        List<TopSearchNews> allNewsList = topSearchNewsDao.findAll();
        Map<String, List<TopSearchNews>> newsMap = allNewsList.stream()
                .collect(Collectors.groupingBy(TopSearchNews::getSource));

        // 创建返回的对象
        List<Map<String, List>> list = new ArrayList<>();
        newsMap.forEach((source, newsList) -> {
            Map<String, List> map = new HashMap<>();
            map.put("hotSearchItems", newsList);
            list.add(map);
        });
        return list;
    }
    public void saveAll(List<TopSearchNews> list){topSearchNewsDao.saveAll(list);}
    public void deleteAllInBatch(){topSearchNewsDao.deleteAllInBatch();}
    public TopSearchNews createEntity(String title, String url, String source, LocalDate date){
        TopSearchNews topSearchNews=new TopSearchNews();
        topSearchNews.setTitle(title);
        topSearchNews.setUrl(url);
        topSearchNews.setSource(source);
        topSearchNews.setDate(date);
        return topSearchNews;
    }
}