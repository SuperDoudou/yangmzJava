package com.yangmz.base.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BaseJson {
    public BaseJson(){
    }

    public static String toJson(Map para){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(para);
    }

    public static String toJson(Boolean result, String info){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,String> para = new HashMap<String, String>();
        if (result){
            para.put("result","success");
        }else{
            para.put("result","fail");
        }
        para.put("info",info);
        return gson.toJson(para);
    }

    public static String toJson(Boolean result, String key, String value){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,String> para = new HashMap<String, String>();
        if (result){
            para.put("result","success");
        }else{
            para.put("result","fail");
        }
        para.put(key,value);
        return gson.toJson(para);
    }

    public static String toJson(String result, String info){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,String> para = new HashMap<String, String>();
        para.put("result", result);
        para.put("info",info);
        return gson.toJson(para);
    }
}
