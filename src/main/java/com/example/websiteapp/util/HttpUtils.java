package com.example.websiteapp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;


@Slf4j
@Component // 将HttpUtils声明为Spring管理的Bean
public class HttpUtils {

    private static final CloseableHttpClient httpClient;

    static {
        httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000) // 连接超时时间（毫秒）
                        .setSocketTimeout(5000) // 读取超时时间（毫秒）
                        .build())
                .build();
    }

    public static String get(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url.trim());
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode >= 300) {
                log.error("请求失败，状态码：{}", statusCode);
                throw new Exception("请求失败，状态码：" + statusCode);
            }
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.error("请求异常：{}", e.getMessage(), e);
            throw e; // 直接抛出原始异常
        } finally {
            httpGet.releaseConnection(); // 释放连接
        }
    }

    @PreDestroy // 确保在Spring容器销毁Bean时调用此方法
    public void destroy() {
        try {
            httpClient.close();
            log.info("HttpClient关闭成功");
        } catch (IOException e) {
            log.error("关闭HttpClient时发生异常", e);
        }
    }
}


