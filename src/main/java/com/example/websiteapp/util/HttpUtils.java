package com.example.websiteapp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


@Slf4j
public class HttpUtils {
    public static String get(String url) throws Exception {
        HttpGet httpGet=new HttpGet(url.trim());
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
        try{
            HttpResponse httpResponse= HttpClientBuilder.create().build().execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
