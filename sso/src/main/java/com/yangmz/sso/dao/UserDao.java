package com.yangmz.sso.dao;


import com.yangmz.base.client.MybatisClient;
import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseHelper;
import com.yangmz.base.model.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class UserDao {

    private final MybatisClient mybatisClient;
    private static Logger logger = Logger.getLogger(UserDao.class);
    private Integer userNameLength = 30;
    @Autowired
    public UserDao(MybatisClient mybatisClient) {
        this.mybatisClient = mybatisClient;
    }


    public String createUserRegister(String email,String code){
        String statement = "com.yangmz.sso.model.userMapper.createAccount";
        Long time = System.currentTimeMillis();
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("email",email);
        sqlPara.put("verifyCode", code );
        sqlPara.put("updateTime", String.valueOf(time) );
        sqlPara.put("createTime", String.valueOf(time) );
        try (SqlSession session = mybatisClient.openSession()) {
            session.insert(statement, sqlPara);
            session.commit();
        }
        return "success";
    }

    public Boolean isExistEmail(String email) {
        String statement = "com.yangmz.sso.model.userMapper.getSimpleUserByEmail";
        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement,email);
        }
        if(user != null && user.getId() > 0){
            return true;
        }
        return false;
    }

    public String updateToken(String userId) {
        Long longUserId = null;
        try {
            longUserId = Long.valueOf(userId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longUserId == null || longUserId <=0 ){
            return null;
        }
        String token = BaseHelper.generateToken();
        String statement = "com.yangmz.sso.model.userMapper.updateToken";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("id",String.valueOf(longUserId) );
        sqlPara.put("token", token );
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, sqlPara);
            session.commit();
        }

        return token;
    }

    public void updateVerifyCode(String email, String code) {
        String statement = "com.yangmz.sso.model.userMapper.updateVerifyCode";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        Long time = System.currentTimeMillis();

        sqlPara.put("email",email);
        sqlPara.put("verifyCode", code );
        sqlPara.put("verifyCodeRetry", String.valueOf(0) );
        sqlPara.put("verifyCodeTime", String.valueOf(time) );
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, sqlPara);
            session.commit();
        }
        return;
    }

    public User getUserByPassword(String email, String password) {
        if( email == null || password == null){
            return null;
        }
        String statement = "com.yangmz.sso.model.userMapper.getUserByPassword";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        String passwordMixed = password + Env.SaltWord;
        String passwordMD5 = BaseHelper.MD5(passwordMixed);
        sqlPara.put("email",email);
        sqlPara.put("password", passwordMD5 );
        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement, sqlPara);
        }
        return user;
    }

    /*
    通过email 获取登陆的用户信息
     */
    public User getLoginUserByEmail(String email) {
        if( email == null){
            return null;
        }
        String statement = "com.yangmz.sso.model.userMapper.getLoginUserByEmail";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("email",email);
        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement, sqlPara);
        }
        return user;
    }

    public User getUserByToken(String userId, String token) {
        Long longUserId = null;
        try {
            longUserId = Long.valueOf(userId);
        }catch (NumberFormatException e){
            return null;
        }
        if ( longUserId == null || longUserId <=0 ){
            return null;
        }
        String statement = "com.yangmz.sso.model.userMapper.getUserByToken";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("id",longUserId.toString());
        sqlPara.put("token", token );
        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement, sqlPara);
        }
        return user;
    }

    public User createUser(String email) {
        logger.info("create User email is " + email);


        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("email",email);
        String userName = email + "_" + BaseHelper.generateRandCode(6);
        if ( userName.length() > userNameLength){
            userName = userName.substring(0,userNameLength);
        }
        sqlPara.put("name",userName);
        String passwordMixed = "" + Env.SaltWord;
        String passwordMD5 = BaseHelper.MD5(passwordMixed);
        sqlPara.put("password",  passwordMD5);
        sqlPara.put("portraitAddress", "defaultPortrait.png");
        Long time = System.currentTimeMillis();
        sqlPara.put("updateTime", String.valueOf(time) );
        sqlPara.put("createTime", String.valueOf(time) );
        User user = null;
        String statement = "com.yangmz.sso.model.userMapper.createUser";
        try (SqlSession session = mybatisClient.openSession()) {
            session.insert(statement, sqlPara);
            session.commit();
            statement = "com.yangmz.sso.model.userMapper.getLoginUserByEmail";
            user = session.selectOne(statement, email);
        }

        return user;
    }
}
