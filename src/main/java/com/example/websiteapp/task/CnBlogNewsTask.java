package com.example.websiteapp.task;

import com.example.websiteapp.entity.CnBlogNews;
import com.example.websiteapp.service.CnBlogNewsService;
import com.example.websiteapp.util.HttpUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CnBlogNewsTask implements Runnable{
    private String cnBlogUrl;
    @Autowired
    CnBlogNewsService cnBlogNewsService;
    public void setCnBlogUrl(String cnBlogUrl) {
        this.cnBlogUrl = cnBlogUrl;
    }
    SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd hh:mm");


    @Override
    public void run(){
        try{
            getNews();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getNews() throws Exception {
        String cnBlogResult = HttpUtils.get(cnBlogUrl);
        Document doc = Jsoup.parse(cnBlogResult);
        Elements elements = doc.select("body>div[id=main_wrapper]>div[id=sideleft]>div[id=news_list]").select(">div[class=news_block]");
        for (Element ele : elements) {
            String title = ele.select("h2[class=news_entry]").text();
            //防重判断
            if (cnBlogNewsService.countByTitle(title) > 0) {
                continue;
            }
            String url = cnBlogUrl + ele.select("a").attr("href");
            String content=ele.select("div[class=content]>div[class=entry_summary]").text();
            Date date=formatter.parse(ele.select("div[class=content]>div[class=entry_footer]>span[class=gray]").text());
            CnBlogNews blogNews = new CnBlogNews();
            blogNews.setTitle(title);
            blogNews.setUrl(url);
            blogNews.setContent(content);
            blogNews.setDate(date);
            cnBlogNewsService.insertOrUpdate(blogNews);
        }
    }
}
