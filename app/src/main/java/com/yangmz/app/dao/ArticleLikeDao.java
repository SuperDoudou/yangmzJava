package com.yangmz.app.dao;

import com.yangmz.app.model.ArticleComment;
import com.yangmz.app.model.ArticleImage;
import com.yangmz.app.model.ArticleLike;
import com.yangmz.app.model.CommentList;
import com.yangmz.base.client.MybatisClient;
import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseValue;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleLikeDao {

    private final MybatisClient mybatisClient;

    @Autowired
    public ArticleLikeDao(MybatisClient mybatisClient) {
        this.mybatisClient = mybatisClient;
    }


    public Boolean createArticleLike(String userId, String articleId){
        Long longUserId = null;
        Long longArticleId = null;

        longUserId = BaseValue.getLongId(userId);
        longArticleId = BaseValue.getLongId(articleId);

        if ( longUserId == null || longUserId <=0
            ||longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleLikeMapper.createArticleLike";
        Long time = System.currentTimeMillis();
        ArticleLike articleLike = new ArticleLike();
        articleLike.setUserId(longUserId);
        articleLike.setArticleId(longArticleId);
        articleLike.setUpdateTime(time);
        articleLike.setCreateTime(time);
        try (SqlSession session = mybatisClient.openSession()) {
            session.insert(statement, articleLike);
            session.commit();
        }
        return true;
    }

    /*
    return null for nothing
           true get valid like
           false get disable like
     */
    public Boolean getArticleLike(String userId, String articleId){
        Long longUserId = null;
        Long longArticleId = null;

        longUserId = BaseValue.getLongId(userId);
        longArticleId = BaseValue.getLongId(articleId);

        if ( longUserId == null || longUserId <=0
                ||longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleLikeMapper.getArticleLike";
        Map<String,String> params = new HashMap<>();
        params.put("userId",userId);
        params.put("articleId",articleId);

        ArticleLike articleLike = null;
        try (SqlSession session = mybatisClient.openSession()){
            articleLike = session.selectOne(statement, params);
        }
        if ( articleLike == null ){
            return null;
        }
        if (!articleLike.getValid().equals(1)){
            return false;
        }
        return true;
    }

    public List<ArticleLike> getArticleLikeList(String userId, String articleId){
        Long longUserId = null;
        Long longArticleId = null;

        longUserId = BaseValue.getLongId(userId);
        longArticleId = BaseValue.getLongId(articleId);

        if ( longUserId == null || longUserId <=0
                ||longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleLikeMapper.getArticleLikeList";
        Map<String,String> params = new HashMap<>();
        params.put("userId",userId);
        params.put("articleId",articleId);
        List<ArticleLike> articleLike = null;
        try (SqlSession session = mybatisClient.openSession()) {
            articleLike = session.selectList(statement, params);
        }
        return articleLike;
    }

    public Boolean disableArticleLike(String userId, String articleId){
        Long longUserId = null;
        Long longArticleId = null;
        longUserId = BaseValue.getLongId(userId);
        longArticleId = BaseValue.getLongId(articleId);

        if ( longUserId == null || longUserId <=0
                ||longArticleId == null || longArticleId <=0 ){
            return null;
        }
        updateArticleLike(userId, articleId, 0);
        return true;
    }

    public Boolean enableArticleLike(String userId, String articleId){
        Long longUserId = null;
        Long longArticleId = null;
        longUserId = BaseValue.getLongId(userId);
        longArticleId = BaseValue.getLongId(articleId);

        if ( longUserId == null || longUserId <=0
                ||longArticleId == null || longArticleId <=0 ){
            return null;
        }
        updateArticleLike(userId, articleId, 1);
        return true;
    }

    public Boolean updateArticleLike(String userId, String articleId, Integer valid){
        Long longUserId = null;
        Long longArticleId = null;

        longUserId = BaseValue.getLongId(userId);
        longArticleId = BaseValue.getLongId(articleId);

        if ( longUserId == null || longUserId <=0
                ||longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleLikeMapper.updateArticleLike";
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("articleId",articleId);
        params.put("valid", valid);
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, params);
            session.commit();
        }
        return true;
    }


    public Integer removeExtraArticleLike(List<ArticleLike> articleLikeList) {

        List<Long> idList = new ArrayList<>();
        Boolean isFirstLike = true;
        Long minId = 0L;
        for(ArticleLike like : articleLikeList){
            if (isFirstLike) {
                isFirstLike = false;
                minId = like.getId();
                continue;
            }
            if (like.getId() < minId) {
                idList.add(minId);
                minId = like.getId();
                continue;
            }
            idList.add(like.getId());
        }

        String statement = "com.yangmz.app.model.ArticleLikeMapper.removeExtraArticleLike";
        Integer deleteRow = null;
        try (SqlSession session = mybatisClient.openSession()) {
            deleteRow = session.delete(statement, idList);
            session.commit();
        }
        return deleteRow;
    }
}
