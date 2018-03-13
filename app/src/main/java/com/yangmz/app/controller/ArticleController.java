package com.yangmz.app.controller;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yangmz.app.base.BaseString;
import com.yangmz.app.dao.ArticleLikeDao;
import com.yangmz.app.model.*;
import com.yangmz.app.base.TextItem;
import com.yangmz.app.dao.ArticleCommentDao;
import com.yangmz.app.dao.ArticleDao;
import com.yangmz.app.dao.ArticleImageDao;
import com.yangmz.base.dao.ImageDao;
import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseHelper;
import com.yangmz.base.helper.BaseRestful;
import com.yangmz.base.helper.BaseValue;
import com.yangmz.base.helper.Error;
import com.yangmz.base.json.BaseJson;
import com.yangmz.base.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticleController {

    private static Logger logger = Logger.getLogger(ArticleController.class);

    private BaseString baseString = new BaseString();
    private ImageDao imageDao = null;
    private ArticleImageDao articleImageDao = null;
    private ArticleDao articleDao = null;
    private ArticleCommentDao articleCommentDao = null;
    private ArticleLikeDao articleLikeDao = null;
    @Autowired
    public ArticleController(ImageDao imageDao,
                             ArticleImageDao articleImageDao,
                             ArticleDao articleDao,
                             ArticleCommentDao articleCommentDao,
                             ArticleLikeDao articleLikeDao) {
        this.imageDao = imageDao;
        this.articleImageDao = articleImageDao;
        this.articleDao = articleDao;
        this.articleCommentDao = articleCommentDao;
        this.articleLikeDao = articleLikeDao;
    }


    /**
     * input URL is /app/home
     *
     * @param request e
     * @return result
     */
    @RequestMapping(value = "/home",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.GET)
    @ResponseBody
    public String home(HttpServletRequest request) {
        logger.info("Enter to home");
        return "thank to view";
    }

    /**
     * input URL is /app/getArticleList/page/{page}/order/{type}

     *
     * @param request e
     * @return result
     */
    @RequestMapping(value = "/getArticleList/page/{page}/order/{order}",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.GET)
    @ResponseBody
    public String getArticleList(@PathVariable("page") String pagePara,@PathVariable("order") String orderPara,
                                 HttpServletRequest request) {
        if(!orderPara.equals("time") && !orderPara.equals("like")){
            return BaseJson.toJson("fail", "order error");
        }
        logger.info("Enter to getArticleList");
        List<Article> articleListData = articleDao.getArticleList(pagePara,orderPara);
        if (articleListData == null || articleListData.size() == 0) {
            return BaseJson.toJson("fail", "page error");
        }
        ArticleList articleList = new ArticleList();
        articleList.setArticleList(articleListData);

        List<Long> articleIdList = articleList.getArticleIdList();
        List<ArticleImage> imageList = articleImageDao.getHeadImageByArticleIdList(articleIdList);
        matchArticlesImage(articleList.getArticleList(),imageList);

        //获取user信息
        String userIdList = articleList.getUserIdList();
        String userService = Env.ServiceAddress + ":" + Env.ServicePort;
        try {
            userIdList = URLEncoder.encode(userIdList,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String userListJson = BaseRestful.httpGet(userService + Env.GetUserList + userIdList);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        List<User> userList = gson.fromJson(userListJson,type);
        //
        matchArticlesUsers(articleList.getArticleList(),userList);
        articleList.image2Min();
        return articleList.toJson();
    }

    private void matchArticlesImage(List<Article> articleList, List<ArticleImage> imageList) {
        for ( Article article : articleList){
            for ( ArticleImage articleImage : imageList){
                if (article.getId().equals(articleImage.getArticleId())){
                    article.setHeadImageAddress(articleImage.getAddress());
                }
            }
        }
    }

    private void matchArticlesUsers(List<Article> articleList, List<User> userList) {
        for ( Article article : articleList){
            for ( User user : userList){
                if (article.getUserId().equals(user.getId())){
                    article.setUser(user);
                }
            }
        }
    }


    /**
     * input URL is /app/getArticle/{id}
     *
     * @param request e
     * @return result
     */
    @RequestMapping(value = "/getArticle/{id}",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.GET)
    @ResponseBody
    public String getArticle(@PathVariable("id") String articleId, HttpServletRequest request) {
        logger.info("Enter to getArticle");
        Article article = articleDao.getArticleById(articleId);
        if (article == null) {
            return BaseJson.toJson("fail", "id error");
        }
        List<ArticleImage> articleImages = articleImageDao.getArticleImageByArticleId(articleId);
        articleDao.increaseViewNum(articleId);
        return article.toHtmlJson(articleImages);
    }


    /**
     * input URL is /app/getArticle/{id}
     *
     * @param request e
     * @return result
     */
    @RequestMapping(value = "/getArticle/comment/{id}/page/{page}",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.GET)
    @ResponseBody
    public String getArticleComment(@PathVariable("id") String articleIdPara,
                                    @PathVariable("page") String pagePara, HttpServletRequest request) {

        logger.info("Enter to getArticleComment");
        Article article = articleDao.getArticleById(articleIdPara);
        if (article == null) {
            return BaseJson.toJson("fail", "article id error");
        }

        CommentList commentList = articleCommentDao.getArticleCommentPageByArticleId(articleIdPara,pagePara);
        if(commentList == null){
            return BaseJson.toJson("fail", "articleId or page wrong");
        }
        for (ArticleComment comment: commentList.getCommentList()) {
            Long userId = comment.getUserId();
            User user = BaseRestful.getUser(userId.toString());
            if (user == null){
                return BaseJson.toJson("fail", "user id error");
            }
            comment.setUser(user);
        }
        return commentList.toJson();
    }



    /**
     * input URL is /app/uploadArticle/image
     *
     * @param request e
     *                userId - id
     *                token  - token
     * @return result
     */
    @RequestMapping(value = "/uploadArticle/image",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(HttpServletRequest request) {
        logger.info("Enter to uploadImage");
        String userId = request.getParameter("userId");
        String index = request.getParameter("index");
        Long time = System.currentTimeMillis();
        String fileName = String.valueOf(time) + "_" + userId + ".jpg";
        String filePath = BaseString.ArticleImagePath + BaseString.ArticleCachePath + "/" + fileName;
        String absolutePath = Env.HomePath + filePath;
        try {
            imageDao.saveArticleImage(absolutePath, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Integer width = BaseHelper.getWidth(absolutePath);
        Integer height = BaseHelper.getHeight(absolutePath);
        ArticleImage articleImage = articleImageDao.createArticleImage(userId, filePath, true, width, height);
        articleImage.setIndex(Integer.valueOf(index));
        return articleImage.toJson();
    }

    /**
     * input URL is /app/uploadArticle/text
     *
     * @param request e
     *                email - email
     *                code    VerifyCode or Password
     * @return result
     */
    @RequestMapping(value = "/uploadArticle/text",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String uploadArticle(HttpServletRequest request) {
        logger.info("Enter to uploadArticle");
        String userId = request.getParameter("userId");
        String title = request.getParameter("title");
        String text = request.getParameter("text");
        Error e = new Error();
        Article article = articleDao.createArticle(userId, title, text, e);
        if (article == null) {
            return BaseJson.toJson("fail", e.getError());
        }
        if (!transportImage4Article(article)) {
            articleDao.deleteArticleById(article.getId());
            return BaseJson.toJson("fail", "image transport error");
        }
        return article.toJson();
    }

    /**
     * input URL is /uploadArticle/comment
     *
     * @param request e
     *                email - email
     *                code    VerifyCode or Password
     * @return result
     */
    @RequestMapping(value = "/uploadArticle/comment",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String uploadArticleComment(HttpServletRequest request) {
        logger.info("Enter to uploadArticleComment");
        String userId = request.getParameter("userId");
        String articleId = request.getParameter("articleId");
        String comment = request.getParameter("comment");
        //校验评论长度
        if(comment == null || comment.length()<10 ){
            return BaseJson.toJson("fail", "comment too short");
        }
        //检查article
        if(articleId == null || articleDao.getArticleById(articleId)==null ){
            return BaseJson.toJson("fail", "articleId wrong");
        }
        ArticleComment articleComment = articleCommentDao.createArticleComment(userId, articleId, comment);
        if (articleComment == null) {
            return BaseJson.toJson("fail", "article error");
        }
        articleDao.increaseCommentNum(articleId);
        return articleComment.toJson();
    }

    /**
     * input URL is /uploadArticle/comment
     *
     * @param request e
     *                email - email
     *                code    VerifyCode or Password
     * @return result
     */
    @RequestMapping(value = "/uploadArticle/like",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String uploadArticleLike(HttpServletRequest request) {
        logger.info("Enter to uploadArticleLike");
        String userId = request.getParameter("userId");
        String articleId = request.getParameter("articleId");

        //检查article
        if(articleId == null || articleDao.getArticleById(articleId) == null ){
            return BaseJson.toJson("fail", "articleId wrong");
        }
        Boolean isLikeValid = articleLikeDao.getArticleLike(userId, articleId);
        //no like record
        if(isLikeValid == null ){
            articleLikeDao.createArticleLike(userId, articleId);
            articleDao.increaseLikeNum(articleId);
            List<ArticleLike> ArticleLikeList = articleLikeDao.getArticleLikeList(userId, articleId);
            if( ArticleLikeList == null ){
                return BaseJson.toJson("fail","like article fail");
            }
            if( ArticleLikeList.size() == 1 ){
                return BaseJson.toJson("success","like article success");
            }
            Integer row = articleLikeDao.removeExtraArticleLike(ArticleLikeList);
            articleDao.decreaseLikeNum(articleId,row);
            return BaseJson.toJson("success","like article success");
        }

        //is Like
        if(isLikeValid){
            articleLikeDao.disableArticleLike(userId, articleId);
            articleDao.decreaseLikeNum(articleId,1);
            return BaseJson.toJson("success","unlike article success");
        }
        // like is disabled
        articleLikeDao.enableArticleLike(userId, articleId);
        articleDao.increaseLikeNum(articleId);
        return BaseJson.toJson("success","relike article success");
    }

    /**
     * input URL is /getArticle/like/{id}
     *
     * @param request e
     *                email - email
     *                code    VerifyCode or Password
     * @return result
     */
    @RequestMapping(value = "/getArticle/like/{id}",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String getArticleLike(@PathVariable("id") String articleIdPara, HttpServletRequest request) {
        logger.info("Enter to getArticleLike");
        String userId = request.getParameter("userId");
        String articleId = articleIdPara;

        Boolean isLikeValid = articleLikeDao.getArticleLike(userId, articleId);
        if (isLikeValid == null || !isLikeValid) {
            return BaseJson.toJson("false", "no like");
        }
        return BaseJson.toJson("true", "is like");
    }

    /**
     * 将cache图片转换为article图片，涉及磁盘移动，创建缩略图、修改数据库
     * @param article
     * @return
     */
    private Boolean transportImage4Article(Article article) {
        //handling article's image
        //change image's articleId
        String dirname = String.valueOf(article.getId() / Env.ArticlePerDir);
        String Path = BaseString.ArticleImagePath + "/" + dirname + "/";
        Boolean isFirst = true;
        for (TextItem item : article.getItemList()) {
            Long time = System.currentTimeMillis();
            String destFileName = time.toString() + "_" + article.getId().toString();
            destFileName = destFileName + "_" + item.getImageId() + ".jpg";
            File file = new File(Env.HomePath + item.getImageAddress());
            File destFile = new File(Env.HomePath + Path + destFileName);
            BaseHelper.mkdir(destFile.getParent());
            if (!imageDao.createImageThumbnail(file, destFile) || !file.renameTo(destFile)) {
                return false;
            }
            articleImageDao.updateImageToArticle(
                    item.getImageId().toString(), article.getId().toString(), Path + destFileName);
            if(!isFirst) {
                continue;
            }
            isFirst = false;
            articleImageDao.updateImageToHead(item.getImageId().toString());
        }
        return true;
    }
}