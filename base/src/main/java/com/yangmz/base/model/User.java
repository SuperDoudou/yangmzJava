package com.yangmz.base.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yangmz.base.helper.BaseHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    Long id;
    String name;
    String email;
    String verifyCode;
    String portraitAddress;
    Long verifyCodeRetry;
    Long verifyCodeTime;
    String token;

    Long weiboDetailValid;
    Long wechatDetailValid;
    Long taobaoDetailValid;
    String weiboDetail;
    String wechatDetail;
    String taobaoDetail;


    public User imageToNormal(){
        portraitAddress = BaseHelper.imagePathToNormal(this.getPortraitAddress());
        return this;
    }

    public User fromJson(String json) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        User user = gson.fromJson(json,this.getClass());
        if(user.id != null){
            setId(user.id);
        }
        if(user.name != null){
            setName(user.name);
        }
        if(user.portraitAddress != null){
            setPortraitAddress(user.portraitAddress);
        }
        return this;

    }

    public String toJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,String> para = new HashMap<>();
        if (this.id == null || id<=0){
            para.put("result","fail");
            return gson.toJson(para);
        }
        para.put("result","success");
        para.put("id",String.valueOf(id));
        para.put("name",getName());
        para.put("token",token);
        para.put("portraitAddress",portraitAddress);
        return gson.toJson(para);
    }

    public String toDetailJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,Object> para = new HashMap<>();
        if (this.id == null || id<=0){
            para.put("result","fail");
            return gson.toJson(para);
        }
        para.put("result","success");
        para.put("id",String.valueOf(id));
        para.put("name",getName());
        para.put("token",token);
        para.put("portraitAddress",portraitAddress);
        para.put("taobaoDetailValid",taobaoDetailValid);
        para.put("wechatDetailValid",wechatDetailValid);
        para.put("weiboDetailValid",weiboDetailValid);
        para.put("taobaoDetail",taobaoDetail);
        para.put("wechatDetail",wechatDetail);
        para.put("weiboDetail",weiboDetail);
        return gson.toJson(para);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", portraitAddress='" + portraitAddress + '\'' +
                ", verifyCodeRetry=" + verifyCodeRetry +
                ", verifyCodeTime=" + verifyCodeTime +
                ", token='" + token + '\'' +
                '}';
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getPortraitAddress() {
        return portraitAddress;
    }

    public void setPortraitAddress(String portraitAddress) {
        this.portraitAddress = portraitAddress;
    }

    public Long getVerifyCodeRetry() {
        return verifyCodeRetry;
    }

    public void setVerifyCodeRetry(Long verifyCodeRetry) {
        this.verifyCodeRetry = verifyCodeRetry;
    }

    public Long getVerifyCodeTime() {
        return verifyCodeTime;
    }

    public void setVerifyCodeTime(Long verifyCodeTime) {
        this.verifyCodeTime = verifyCodeTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getWeiboDetailValid() {
        return weiboDetailValid;
    }

    public void setWeiboDetailValid(Long weiboDetailValid) {
        this.weiboDetailValid = weiboDetailValid;
    }

    public Long getWechatDetailValid() {
        return wechatDetailValid;
    }

    public void setWechatDetailValid(Long wechatDetailValid) {
        this.wechatDetailValid = wechatDetailValid;
    }

    public Long getTaobaoDetailValid() {
        return taobaoDetailValid;
    }

    public void setTaobaoDetailValid(Long taobaoDetailValid) {
        this.taobaoDetailValid = taobaoDetailValid;
    }

    public String getWeiboDetail() {
        return weiboDetail;
    }

    public void setWeiboDetail(String weiboDetail) {
        this.weiboDetail = weiboDetail;
    }

    public String getWechatDetail() {
        return wechatDetail;
    }

    public void setWechatDetail(String wechatDetail) {
        this.wechatDetail = wechatDetail;
    }

    public String getTaobaoDetail() {
        return taobaoDetail;
    }

    public void setTaobaoDetail(String taobaoDetail) {
        this.taobaoDetail = taobaoDetail;
    }

    public static String toJson(List<User> userList) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        return gson.toJson(userList);
    }
}
