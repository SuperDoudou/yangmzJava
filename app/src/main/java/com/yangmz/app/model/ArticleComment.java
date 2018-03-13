package com.yangmz.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yangmz.base.model.User;

import java.util.HashMap;
import java.util.Map;

public class ArticleComment {
    Long id;
    Long userId;
    User user;
    Long articleId;
    String comment;
    Long createTime;
    Long updateTime;

    public String toJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,String> para = new HashMap<>();
        if (this.id == null || id<=0){
            para.put("result","fail");
            return gson.toJson(para);
        }
        para.put("result","success");
        para.put("id",id.toString());
        para.put("userId",userId.toString());
        para.put("articleId",articleId.toString());
        para.put("comment",comment);
        para.put("createTime",createTime.toString());
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
