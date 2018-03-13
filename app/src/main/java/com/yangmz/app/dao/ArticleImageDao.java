package com.yangmz.app.dao;

import com.yangmz.app.model.ArticleImage;
import com.yangmz.base.client.MybatisClient;
import com.yangmz.base.helper.BaseValue;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleImageDao {

    private final MybatisClient mybatisClient;

    @Autowired
    public ArticleImageDao(MybatisClient mybatisClient) {
        this.mybatisClient = mybatisClient;
    }


    public ArticleImage createArticleImage(String userId,String address,Boolean isCache,Integer width, Integer height){
        Long longUserId = null;
        try {
            longUserId = Long.valueOf(userId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longUserId == null || longUserId <=0 ){
            return null;
        }

        String statement = "com.yangmz.app.model.ArticleImageMapper.createArticleImage";
        Long time = System.currentTimeMillis();
        ArticleImage articleImage = new ArticleImage();
        articleImage.setUserId(longUserId);
        articleImage.setAddress(address);
        if(isCache){
            articleImage.setIsCache(1);
        }else{
            articleImage.setIsCache(0);
        }
        articleImage.setUpdateTime(time);
        articleImage.setCreateTime(time);
        articleImage.setWidth(width);
        articleImage.setHeight(height);
        try (SqlSession session = mybatisClient.openSession()){
            session.insert(statement,articleImage);
            session.commit();
        }
        return articleImage;
    }

    public ArticleImage getArticleImageById(String imageId){
        Long longImageId = null;
        try {
            longImageId = Long.valueOf(imageId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longImageId == null || longImageId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleImageMapper.getArticleImageById";
        ArticleImage articleImage = null;
        try (SqlSession session = mybatisClient.openSession()){
            articleImage = session.selectOne(statement,longImageId);
        }
        return articleImage;
    }

    public List<ArticleImage> getArticleImageByArticleId(String articleId) {
        Long longArticleId = null;
        try {
            longArticleId = Long.valueOf(articleId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleImageMapper.getArticleImageByArticleId";
        List<ArticleImage> articleImages = null;
        try (SqlSession session = mybatisClient.openSession()){
            articleImages = session.selectList(statement,longArticleId);
        }
        return articleImages;
    }

    public Boolean updateImageToArticle(String imageId,String articleId,String address){
        Long longImageId = null;
        Long longArticleId = null;
        try {
            longImageId = Long.valueOf(imageId);
            longArticleId = Long.valueOf(articleId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longImageId == null || longImageId <=0
            ||longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleImageMapper.updateImageToArticle";
        Map<String,String> params = new HashMap<>();
        params.put("id",longImageId.toString());
        params.put("address",address);
        params.put("articleId",longArticleId.toString());
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, params);
            session.commit();
        }
        return true;
    }

    public List<ArticleImage> getHeadImageByArticleIdList(List<Long> idList) {
        if (idList == null || idList.size() <= 0){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleImageMapper.getHeadImageByArticleId";
        List<ArticleImage> articleImages = null;
        try (SqlSession session = mybatisClient.openSession()){
            articleImages = session.selectList(statement,idList);
        }
        return articleImages;
    }

    public Boolean updateImageToHead(String imageId) {
        Long id = BaseValue.getLongId(imageId);
        if(id == null){
            return false;
        }
        String statement = "com.yangmz.app.model.ArticleImageMapper.updateImageToHead";
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, id);
            session.commit();
        }
        return true;
    }
}
