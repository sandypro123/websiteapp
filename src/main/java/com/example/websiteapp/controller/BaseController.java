package com.example.websiteapp.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;

@RestController
public class BaseController {

    public void sealSuccess(DeferredResult deferredResult,Object data){
        HashMap<String,Object> resultMap=new HashMap<>();
        resultMap.put("data",data);
        deferredResult.setResult(resultMap);
    }
}
