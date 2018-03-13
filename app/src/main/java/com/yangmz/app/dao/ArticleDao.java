package com.yangmz.app.dao;

import com.yangmz.app.base.TextItem;
import com.yangmz.app.model.Article;
import com.yangmz.app.model.ArticleImage;
import com.yangmz.app.model.ArticleList;
import com.yangmz.base.client.MybatisClient;
import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseValue;
import com.yangmz.base.helper.Error;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleDao {

    private final MybatisClient mybatisClient;
    private ArticleImageDao articleImageDao = null;
    @Autowired
    public ArticleDao(MybatisClient mybatisClient,ArticleImageDao articleImageDao) {
        this.mybatisClient = mybatisClient;
        this.articleImageDao = articleImageDao;
    }


    public Article createArticle(String userId, String title, String text, Error e){
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null || longUserId <=0 ){
            e.setError("Wrong user Id");
            return null;
        }
        if (!Article.checkTitle(title)){
            e.setError("Wrong title,length less than " + Env.ArticleTitileLength);
            return null;
        }
        List<TextItem> items = Article.getItemFromText(text);
        if (items == null || items.size() <= 0){
            e.setError("Wrong items,items length is 0");
            return null;
        }
        //check the image's owner
        for (TextItem item: items) {
            ArticleImage articleImage = articleImageDao.getArticleImageById(String.valueOf(item.getImageId()));
            if ( articleImage == null
                    || !articleImage.getUserId().equals(longUserId)
                    || !articleImage.getArticleId().equals(0L) ){
                e.setError("check the image's owner wrong");
                return null;
            }
            item.setImageAddress(articleImage.getAddress());
        }

        String statement = "com.yangmz.app.model.ArticleMapper.createArticle";
        Long time = System.currentTimeMillis();
        Article article = new Article();
        article.setUserId(longUserId);
        article.setTitle(title);
        article.setText(text);
        article.setViewNum(0);
        article.setLikeNum(0);
        article.setCommentNum(0);
        article.setUpdateTime(time);
        article.setCreateTime(time);
        try (SqlSession session = mybatisClient.openSession()) {
            session.insert(statement,article);
            session.commit();
        }
        article.setItemList(items);
        return article;
    }


    public Article getArticleById(String articleId) {
        Long longArticleId = null;
        try {
            longArticleId = Long.valueOf(articleId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleMapper.getArticleById";
        Article article = null;
        try (SqlSession session = mybatisClient.openSession()) {
            article = session.selectOne(statement, longArticleId);
        }
        return article;
    }

    public Boolean increaseViewNum(String articleId) {
        Long longArticleId = null;
        try {
            longArticleId = Long.valueOf(articleId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleMapper.increaseViewNum";
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, longArticleId);
            session.commit();
        }
        return true;
    }

    public Boolean increaseCommentNum(String articleId) {
        Long longArticleId = null;
        try {
            longArticleId = Long.valueOf(articleId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleMapper.increaseCommentNum";
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, longArticleId);
            session.commit();
        }
        return true;
    }

    public Boolean increaseLikeNum(String articleId) {
        Long longArticleId = null;
        try {
            longArticleId = Long.valueOf(articleId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleMapper.increaseLikeNum";
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, longArticleId);
            session.commit();
        }
        return true;
    }

    public Boolean decreaseLikeNum(String articleId, Integer num) {
        Long longArticleId = null;
        try {
            longArticleId = Long.valueOf(articleId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longArticleId == null || longArticleId <=0 ){
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id",longArticleId);
        params.put("num",num);
        String statement = "com.yangmz.app.model.ArticleMapper.decreaseLikeNum";
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, params);
            session.commit();
        }
        return true;
    }

    public Boolean deleteArticleById(Long articleId) {
        Long longArticleId = null;
        try {
            longArticleId = Long.valueOf(articleId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleMapper.deleteArticleById";
        try (SqlSession session = mybatisClient.openSession()) {
            session.delete(statement, longArticleId);
            session.commit();
        }
        return true;
    }

    public List<Article> getArticleList(String pagePara, String orderPara) {
        Long longPage = BaseValue.getLongId(pagePara);
        if (longPage == null){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleMapper.getArticleList";

        Map<String,Object> para = new HashMap<>();
        para.put("order",orderPara);
        Long startNum = (longPage-1)* Env.ArticlePerPage;
        para.put("startNum",startNum);
        para.put("itemNum",Env.ArticlePerPage);
        List<Article> articleList = null;
        try (SqlSession session = mybatisClient.openSession()) {
            articleList = session.selectList(statement, para);
        }
        return articleList;
    }
}
