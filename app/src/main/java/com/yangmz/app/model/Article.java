package com.yangmz.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yangmz.app.base.TextItem;
import com.yangmz.base.env.Env;
import com.yangmz.base.model.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Article {
    Long id;
    Long userId;
    User user;
    String headImageAddress;
    String title;
    String text;
    List<TextItem> itemList;
    Integer viewNum;
    Integer likeNum;
    Integer commentNum;
    Long updateTime;
    Long createTime;


    public static List<TextItem> getItemFromText(String text) {
        List<TextItem> textItemList = new LinkedList<TextItem>();
        String[] rawItems = text.split("imageId_yangmz=\\{");
        for(String rawItem : rawItems){
            if(rawItem == null || rawItem.length()==0){
                continue;
            }
            String[] arrayString = rawItem.split("}text_yangmz=\\{",2);
            if( arrayString.length != 2){
                return null;
            }

            Long itemImageId = Long.valueOf(arrayString[0]);
            if(itemImageId == null){
                return null;
            }
            if(!arrayString[1].endsWith("}")){
                return null;
            }
            String itemText = arrayString[1].substring(0,arrayString[1].length()-1);
            TextItem Item = new TextItem(itemImageId,itemText);
            textItemList.add(Item);
        }
        return textItemList;
    }

    public static Boolean checkTitle(String title) {
        if (title == null || title.length() < Env.ArticleTitileLength ){
            return false;
        }
        return true;
    }

    public String toHtmlJson(List<ArticleImage> articleImages) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        itemList = getItemFromText(text);
        for( ArticleImage articleImage: articleImages){
            if( itemList == null){
                break;
            }
            for (TextItem textItem: itemList){
                if( articleImage.getId().equals( textItem.getImageId()) ){
                    textItem.setImageAddress(articleImage.getAddress());
                }
            }
        }

        Map<String,Object> para = new HashMap<>();

        if (this.id == null || itemList == null || id<=0){
            para.put("result","fail");
            return gson.toJson(para);
        }
        para.put("result","success");
        para.put("id",String.valueOf(id));
        para.put("createTime",String.valueOf(createTime));
        para.put("userId",String.valueOf(userId));
        para.put("userName",user.getName());
        para.put("userPortraitAddress",user.getPortraitAddress());

        para.put("title",title);
        para.put("viewNum",String.valueOf(viewNum));
        para.put("likeNum",String.valueOf(likeNum));
        para.put("commentNum",String.valueOf(commentNum));

        para.put("itemList",itemList);
        para.put("numOfItem", itemList.size());
        return gson.toJson(para);
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
        para.put("title",title);
        para.put("text",text);
        para.put("view",String.valueOf(viewNum));
        para.put("like",String.valueOf(likeNum));
        para.put("comment",String.valueOf(commentNum));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TextItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TextItem> itemList) {
        this.itemList = itemList;
    }

    public String getHeadImageAddress() {
        return headImageAddress;
    }

    public void setHeadImageAddress(String headImageAddress) {
        this.headImageAddress = headImageAddress;
    }
}
