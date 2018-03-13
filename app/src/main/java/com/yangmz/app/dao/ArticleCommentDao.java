package com.yangmz.app.dao;

import com.yangmz.app.model.CommentList;
import com.yangmz.app.model.ArticleComment;
import com.yangmz.app.model.ArticleImage;
import com.yangmz.base.client.MybatisClient;
import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseValue;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleCommentDao {

    private final MybatisClient mybatisClient;

    @Autowired
    public ArticleCommentDao(MybatisClient mybatisClient) {
        this.mybatisClient = mybatisClient;
    }


    public ArticleComment createArticleComment(String userId, String articleId, String comment){
        Long longUserId = null;
        Long longArticleId = null;

        longUserId = BaseValue.getLongId(userId);
        longArticleId = BaseValue.getLongId(articleId);

        if ( longUserId == null || longUserId <=0
                ||longArticleId == null || longArticleId <=0 ){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleCommentMapper.createArticleComment";
        Long time = System.currentTimeMillis();
        ArticleComment articleComment = new ArticleComment();
        articleComment.setUserId(longUserId);
        articleComment.setArticleId(longArticleId);
        articleComment.setComment(comment);

        articleComment.setUpdateTime(time);
        articleComment.setCreateTime(time);
        try (SqlSession session = mybatisClient.openSession()) {
            session.insert(statement, articleComment);
            session.commit();
        }
        return articleComment;
    }

    public List<ArticleImage> getArticleCommentByArticleId(String articleId) {
        Long longArticleId = BaseValue.getLongId(articleId);
        if (longArticleId == null){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleCommentMapper.getArticleCommentByArticleId";
        List<ArticleImage> articleImages = null;
        try (SqlSession session = mybatisClient.openSession()) {
            articleImages = session.selectList(statement, articleId);
        }
        return articleImages;
    }

    public CommentList getArticleCommentPageByArticleId(String articleId, String page) {
        Long longArticleId = BaseValue.getLongId(articleId);
        Long longPage = BaseValue.getLongId(page);
        if (longArticleId == null || longPage == null){
            return null;
        }
        String statement = "com.yangmz.app.model.ArticleCommentMapper.getArticleCommentPageById";
        Map<String,Object> para = new HashMap<>();
        para.put("articleId",articleId);
        Long startNum = (longPage-1)*Env.CommentPerPage;
        para.put("startNum",startNum);
        para.put("itemNum",Env.CommentPerPage);
        List<ArticleComment> commentList = null;
        try (SqlSession session = mybatisClient.openSession()) {
            commentList = session.selectList(statement,para);
        }
        CommentList result = new CommentList();
        result.setCommentList(commentList);
        return result;
    }

}
