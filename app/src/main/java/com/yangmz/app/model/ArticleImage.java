package com.yangmz.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yangmz.base.helper.BaseHelper;

import java.util.HashMap;
import java.util.Map;

public class ArticleImage {
    Long id;
    Long userId;
    Long articleId;
    String address;
    Integer isCache;
    Integer width;
    Integer height;
    Long updateTime;
    Long createTime;
    Integer index = -1;

    public String toJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,String> para = new HashMap<>();
        if (this.id == null || id<=0){
            para.put("result","fail");
            return gson.toJson(para);
        }
        para.put("result","success");
        para.put("id",String.valueOf(id));
        para.put("userId",String.valueOf(userId));
        para.put("articleId",String.valueOf(articleId));
        para.put("address",address);
        if( index != null && index >= 0) {
            para.put("index",String.valueOf(index));
        }
        return gson.toJson(para);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsCache() {
        return isCache;
    }

    public void setIsCache(Integer isCache) {
        this.isCache = isCache;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
