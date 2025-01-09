package com.example.websiteapp.task;

import com.example.websiteapp.entity.CnBlogNews;
import com.example.websiteapp.entity.TopSearchNews;
import com.example.websiteapp.service.AsyncScheduledService;
import com.example.websiteapp.service.CnBlogNewsService;
import com.example.websiteapp.service.TopSearchNewsService;
import com.example.websiteapp.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: sandy
 * \* Date: 2024/11/12
 * \* Time: 17:52
 * \* Description:
 * \
 */
@Component
@Slf4j
public class AsyncScheduledTasks {
    @Value("${cnBlog.welfareUrl}")
    private String cnblogUrl;
    @Value("${topSearchNews.weiboUrl}")
    private String weiboUrl;
    @Value("${topSearchNews.zhihuUrl}")
    private String zhihuUrl;
    @Value("${topSearchNews.doubanUrl}")
    private String doubanUrl;
    @Value("${topSearchNews.shaoshupaiUrl}")
    private String shaoshupaiUrl;

    private AsyncScheduledService scheduledService;
    private CnBlogNewsService cnBlogNewsService;
    private TopSearchNewsService topSearchNewsService;
    @Autowired
    public AsyncScheduledTasks(AsyncScheduledService scheduledService, CnBlogNewsService cnBlogNewsService,
                               TopSearchNewsService topSearchNewsService) {
        this.scheduledService = scheduledService;
        this.cnBlogNewsService = cnBlogNewsService;
        this.topSearchNewsService = topSearchNewsService;
    }

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Scheduled(cron="0 0 0/1 * * *")  // 从0点开始，每隔1小时执行
    public void scheduledTaskSpider(){
        log.info("startTask");
        //scheduledService.executeTask(this::cnBlogSpiderTask);
        scheduledService.executeTask(this::topSearchNewsTask);
    }

    public void cnBlogSpiderTask() {
        try{
            log.info("start task:cnBlogTask");
            String cnBlogResult = HttpUtils.get(cnblogUrl);
            Document doc = Jsoup.parse(cnBlogResult);
            Elements elements = doc.select("body>div[id=main_wrapper]>div[id=sideleft]>div[id=news_list]").select(">div[class=news_block]");
            for (Element ele : elements) {
                String title = ele.select("h2[class=news_entry]").text();
                String url = cnblogUrl + ele.select("a").attr("href");
                String content=ele.select("div[class=content]>div[class=entry_summary]").text();
                Date date=formatter.parse(ele.select("div[class=content]>div[class=entry_footer]>span[class=gray]").text());
                CnBlogNews blogNews = new CnBlogNews();
                blogNews.setTitle(title);
                blogNews.setUrl(url);
                blogNews.setContent(content);
                //blogNews.setDate(date);
                cnBlogNewsService.insertOrUpdate(blogNews);
            }
            log.info("finish task:cnBlogTask");
        }catch (Exception e){
            log.error("Error fetching CnBlog news", e);
        }
    }

    public void topSearchNewsTask(){
        log.info("start task:topSearchNewsTask");
        topSearchNewsService.deleteAllInBatch();
        List<TopSearchNews> list=new ArrayList<>();
        try {
            list.addAll(getWeiboTopNews(weiboUrl,"weibo"));
            list.addAll(getZhiHuTopNews(zhihuUrl,"zhihu"));
            //list.addAll(getDoubanTopNews(doubanUrl,"douban"));
            list.addAll(getShaoShuPaiTopNews(shaoshupaiUrl,"shaoshupai"));
            topSearchNewsService.saveAll(list);
            log.info("finish task:topSearchNewsTask");
        }catch (Exception e){
            log.error("Error fetching top search news", e);
        }
    }


    public List<TopSearchNews> getWeiboTopNews(String url,String source) throws Exception {
        String result = HttpUtils.get(url);
        Map dataMap = JSON.parseObject(result,new TypeReference<Map>(){});
        Map data1 = (Map) dataMap.get("data");
        List<Map<String,String>> hotList = (List)data1.get("realtime");
        List<TopSearchNews> topSearchNewsList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        hotList.forEach(hot->{
            TopSearchNews topSearchNews=topSearchNewsService.createEntity(hot.get("note"),"https://s.weibo.com/weibo?q=%23"+hot.get("note")+"%23",source,currentDate);
                    topSearchNewsList.add(topSearchNews);
        });
        return topSearchNewsList;
    }

    public List<TopSearchNews> getZhiHuTopNews(String url,String source) throws Exception {
        String result = HttpUtils.get(url);
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray dataArray = jsonObject.getJSONArray("data");
        List<TopSearchNews> topSearchNewsList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataObject = dataArray.getJSONObject(i);
            JSONObject targetObject = dataObject.getJSONObject("target");
            String zhihuUrl=targetObject.getString("url");
            zhihuUrl=zhihuUrl.replace("api","www");
            zhihuUrl=zhihuUrl.replace("questions","question");
            TopSearchNews topSearchNews=topSearchNewsService.createEntity(formatText(targetObject.getString("title")),zhihuUrl,source,currentDate);
            topSearchNewsList.add(topSearchNews);
        }
        return topSearchNewsList;
    }

    public List<TopSearchNews> getDoubanTopNews(String url,String source) throws Exception{
        String result=HttpUtils.get(url);
        Document doc = Jsoup.parse(result);
        List<TopSearchNews> topSearchNewsList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        Elements elements = doc.select("body>div[id=wrapper]>div[id=content]>div[class=grid-16-8 clearfix]>div[class=aside]>div[class=mod]>ul[class=trend]").select("li").select("a");
        for (Element ele : elements) {
            String doubanUrl=ele.select("a").attr("href");
            String title=ele.select("a").text();
            title = formatText(title);
            TopSearchNews topSearchNews=topSearchNewsService.createEntity(title,doubanUrl,source,currentDate);
            topSearchNewsList.add(topSearchNews);
        }
        return topSearchNewsList;
    }

    public List<TopSearchNews> getShaoShuPaiTopNews(String url,String source) throws Exception {
        String result = HttpUtils.get(url);
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray dataArray = jsonObject.getJSONArray("data");
        List<TopSearchNews> topSearchNewsList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataObject = dataArray.getJSONObject(i);
            String title=dataObject.getString("title");
            title = formatText(title);
            String shaoshupaiUrl="https://sspai.com/post/"+dataObject.getString("id");
            TopSearchNews topSearchNews=topSearchNewsService.createEntity(title,shaoshupaiUrl,source,currentDate);
            topSearchNewsList.add(topSearchNews);
        }
        return topSearchNewsList;
    }

    public String formatText(String text){
        text= text.replaceAll("[^\\p{IsAlnum}\\p{IsPunct}\\p{IsHan}]", "");
        return text;
    }

}