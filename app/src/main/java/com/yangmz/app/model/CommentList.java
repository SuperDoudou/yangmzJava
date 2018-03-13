package com.yangmz.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yangmz.app.model.ArticleComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentList {
    private List<ArticleComment> commentList = null;

    public CommentList(){
        commentList = new ArrayList<ArticleComment>();
    }

    public Boolean addComment(ArticleComment comment){
        if (comment == null){
            return false;
        }
        commentList.add(comment);
        return true;
    }

    public List<ArticleComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<ArticleComment> commentList) {
        this.commentList = commentList;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,Object> para = new HashMap<>();
        para.put("commentList",commentList);
        para.put("numOfComment",commentList.size());
        return gson.toJson(para);
    }
}
