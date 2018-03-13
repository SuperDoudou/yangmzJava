package com.yangmz.sso.controller;

import com.google.gson.Gson;
import com.yangmz.base.client.Jedis.JedisClient;
import com.yangmz.base.client.Jedis.JedisFactory;
import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseHelper;
import com.yangmz.base.property.EmailProperty;
import com.yangmz.base.json.BaseJson;
import com.yangmz.base.model.User;
import com.yangmz.base.service.EmailService;
import com.yangmz.sso.dao.UserDao;
import com.yangmz.sso.dao.UserVerifyDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class UserSSOController {


    final static String VERIFY_EMAIL_TITLE = "欢迎注册羊毛戳戳";
    final static String VERIFY_EMAIL_TEXT = "您的验证码是num，十分钟内有效。";


    private static Logger logger = Logger.getLogger(UserSSOController.class);
    final EmailProperty emailProperty;
    final EmailService emailService;
    final UserDao userDao;
    final UserVerifyDao userVerifyDao;
    @Autowired
    public UserSSOController(EmailService emailService,
                             EmailProperty emailProperty,
                             UserDao userDao,
                             UserVerifyDao userVerifyDao) {
        this.emailProperty = emailProperty;
        this.emailService = emailService;
        this.userDao = userDao;
        this.userVerifyDao = userVerifyDao;
    }

    @RequestMapping(value = "/home")
    @ResponseBody
    public String home(){
        logger.info("Here is home");
        logger.error("no error");
        return "home";
    }


    /**
     * input URL is /sso/setVerifyByEmail
     * @param request re
     * @return send Email
     */
    @RequestMapping(value = "/setVerifyByEmail",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String sendVerifyCode(HttpServletRequest request)
    {
        String email = request.getParameter("email");
        if( email == null || email.equals("")){
            return BaseJson.toJson("fail","email为空");
        }
        logger.info("Enter getVerifyCode URL: /setVerifyByEmail  email = " + email);

        if( !BaseHelper.isValidEmail(email) ){
            return BaseJson.toJson("fail","email不合法");
        }

        if (!getVerifyCode(email)){
            return BaseJson.toJson("fail","请不要重复获取验证码");
        }
        if(!setVerifyCode(email)){
            return BaseJson.toJson("fail","发送验证码失败");
        }
        return BaseJson.toJson("success","已发送验证码");

    }

    /**
     * input URL is /sso/loginByEmail
     * @param request  e
     * email - email
     * code    VerifyCode or Password
     * @return result
     */
    @RequestMapping(value = "/loginByEmail",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String loginUser(HttpServletRequest request){
        String email = request.getParameter("email");
        String code = request.getParameter("code");
        if( email == null || email.equals("") ||
            code == null || code.equals("")){
            return  BaseJson.toJson(false,"无该用户信息");
        }
        logger.info("Enter getVerifyCode URL: /loginByEmail " +
                "email = " + email + " " +
                "code = " + code);

        //校验Email的合法性
        if( !BaseHelper.isValidEmail(email) ){
            return  BaseJson.toJson(false,"输入正确的Email");
        }

        //该用户是已经注册、访问users表
        if( userDao.isExistEmail(email) ){
            User user = loginByUser(email,code);
            if(user == null){
                return  BaseJson.toJson(false,"输入正确的密码或验证码");
            }
            return user.imageToNormal().toDetailJson();
        }

        //该用户未注册、访问redis userVerify表
        if( userVerifyDao.checkEmailVerifyCode(email,code) ){
            User user = createUser(email);
            if( user == null){
                return  BaseJson.toJson(false,"创建用户失败");
            }
            return user.imageToNormal().toDetailJson();
        }
        return  BaseJson.toJson(false,"无该用户信息");
    }


    /**
     * input URL is /sso/loginByToken
     * @param request  e
     * email - email
     * code    VerifyCode or Password
     * @return result
     */
    @RequestMapping(value = "/loginByToken",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String loginUserByToken(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String token = request.getParameter("token");

        if( userId == null || userId.equals("") ||
                token == null || token.equals(""))
        {
            return BaseJson.toJson("fail","user or token is empty");
        }
        logger.info("Enter loginUserByToken URL: /loginByToken  userId = " + userId);

        User user = userDao.getUserByToken(userId, token);
        if( user == null) {
            return BaseJson.toJson("fail","user no found");
        }
        return user.imageToNormal().toDetailJson();
    }

    /**
     * input URL is /sso/checkToken
     * @param request  e
     * email - email
     * code    VerifyCode or Password
     * @return result
     */
    @RequestMapping(value = "/checkToken",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String checkToken(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String token = request.getParameter("token");

        if( userId == null || userId.equals("") ||
                token == null || token.equals(""))
        {
            return BaseJson.toJson("fail","user or token is empty");
        }

        User user = userDao.getUserByToken(userId, token);
        if( user == null) {
            return BaseJson.toJson("fail","user no found");
        }
        return user.toJson();
    }

    private User createUser(String email) {
        User user = userDao.createUser(email);
        String token = userDao.updateToken(user.getId().toString());
        user.setToken(token);
        return user;
    }

    /**
     *
     * @param email
     * @param code Password or VerifyCode
     * @return user
     */
    private User loginByUser(String email, String code) {
        User user = null;
        if(userVerifyDao.checkEmailVerifyCode(email,code)){
            user = userDao.getLoginUserByEmail(email);
        }
        if (user == null){
            user = userDao.getUserByPassword(email,code);
        }
        if(user != null) {
            String token = userDao.updateToken(user.getId().toString());
            user.setToken(token);
        }
        return user;
    }

    /**
     * 更新用户的验证码
     * @param email
     */
    private Boolean getVerifyCode(String email) {
        String code = userVerifyDao.getEmailVerifyCode(email);
        //查询数据库，有验证码表示不要重复获取
        if (code != null) {
            return false;
        }
        return true;
    }
    private Boolean setVerifyCode(String email) {
        String code = userVerifyDao.setEmailVerifyCode(email);
        //在redis数据库生产验证码失败
        if (code == null){
            return false;
        }
        sendVerifyToEmail(email,code);
        return true;
    }

    private void sendVerifyToEmail(String email,String code){
        String emailText = VERIFY_EMAIL_TEXT.replaceAll("num",code);
        String result = emailService.sendEmail(email,VERIFY_EMAIL_TITLE,emailText);
    }




}
