package com.yangmz.base.helper;

import com.yangmz.base.env.Env;
import com.yangmz.base.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class BaseRestful {


    public static User getUser(String userId){
        String userService = Env.ServiceAddress + ":" + Env.ServicePort;
        userService = userService + Env.GetUser + "/" + userId;
        User user = new User();
        user.fromJson(httpGet(userService));
        if(user.getId() == null){
            return null;
        }
        return user;
    }

    public static String checkUser(String userId, String token){
        String userService = Env.ServiceAddress + ":" + Env.ServicePort;
        userService = userService + Env.CheckTokenService;
        Map<String,String> params = new HashMap<String,String>();
        params.put("userId",userId);
        params.put("token",token);
        return httpPost(userService,params);
    }

    public static String httpPost(String url, Map<String,String> params) {
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        for(Map.Entry<String,String> entry:params.entrySet()){
            list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity formEntity = null;
        try {
            formEntity = new UrlEncodedFormEntity(list,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(formEntity);

        return getHttpResponse(httpPost);
    }

    public static String httpGet(String url) {

        HttpGet httpGet = new HttpGet(url);
        return getHttpResponse(httpGet);

    }

    private static String getHttpResponse(HttpRequestBase requestBase){
        String response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpResponse httpresponse = httpClient.execute(requestBase);
            HttpEntity httpEntity = httpresponse.getEntity();
            response = EntityUtils.toString(httpEntity, "utf-8");
        } catch(IOException e){
            return null;
        }
        return response;
    }
}
