package com.yangmz.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yangmz.base.helper.BaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleList {
    private List<Article> articleList = null;

    public ArticleList(){
        articleList = new ArrayList<Article>();
    }
    public Boolean addArticle(Article article){
        if (article == null){
            return false;
        }
        articleList.add(article);
        return true;
    }

    public List<Long> getArticleIdList() {
        List<Long> idList = new ArrayList<>();
        for (Article article:articleList) {
           idList.add(article.getId());
        }
        return idList;
    }

    public String getUserIdList() {
        Boolean isFirst = true;
        StringBuilder stringBuilder = new StringBuilder();
        for (Article article:articleList) {
            if(isFirst){
                stringBuilder.append(article.getUserId());
                isFirst = false;
            }else{
                stringBuilder.append("|");
                stringBuilder.append(article.getUserId());
            }
        }
        return stringBuilder.toString();
    }


    public void image2Min() {
        for (Article article: articleList){
            if(article.getHeadImageAddress() == null || article.getHeadImageAddress().length() <= 0){
                continue;
            }
            String minPath = BaseHelper.imagePathToMin(article.getHeadImageAddress());
            article.setHeadImageAddress(minPath);
        }
    }

    public String toJson(){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,Object> para = new HashMap<>();
        para.put("articleList",articleList);
        para.put("numOfArticle",articleList.size());
        return gson.toJson(para);
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleListData) {
        this.articleList = articleListData;
    }

}
