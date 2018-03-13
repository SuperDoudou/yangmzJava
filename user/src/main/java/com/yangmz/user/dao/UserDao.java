package com.yangmz.user.dao;


import com.yangmz.base.client.MybatisClient;
import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseHelper;
import com.yangmz.base.helper.BaseValue;
import com.yangmz.base.model.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserDao {

    private final MybatisClient mybatisClient;
    private static Logger logger = Logger.getLogger(UserDao.class);
    private Integer userNameLength = 30;
    private BaseHelper baseHelper = new BaseHelper();
    @Autowired
    public UserDao(MybatisClient mybatisClient) {
        this.mybatisClient = mybatisClient;
    }


    public Boolean isExistEmail(String email) {
        String statement = "com.yangmz.user.model.userMapper.getUserByEmail";
        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement,email);
        }
        if (user == null || user.getId() <= 0){
            return false;
        }
        return true;
    }

    public User getUserByToken(String userId, String token) {
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null){
            return null;
        }
        String statement = "com.yangmz.user.model.userMapper.getUserByToken";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("id",longUserId.toString());
        sqlPara.put("token", token );
        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement, sqlPara);
        }
        return user;
    }

    public Boolean updatePortrait(String userId, String portraitAddress) {
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null){
            return null;
        }
        String statement = "com.yangmz.user.model.userMapper.updateUserPortrait";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("id",longUserId.toString());
        sqlPara.put("portraitAddress", portraitAddress );
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, sqlPara);
            session.commit();
        }
        return true;
    }
    public Boolean updatePassword(String userId, String password) {
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null){
            return null;
        }
        password = password + Env.SaltWord;
        String passwordMD5 = baseHelper.MD5(password);
        String statement = "com.yangmz.user.model.userMapper.updateUserPassword";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("id",longUserId.toString());
        sqlPara.put("password", passwordMD5 );
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, sqlPara);
            session.commit();
        }
        return true;
    }

    public User getUserByPassword(String userId, String password) {
        if( userId == null || password == null){
            return null;
        }
        String statement = "com.yangmz.user.model.userMapper.getUserByPassword";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        String passwordMixed = password + Env.SaltWord;
        String passwordMD5 = BaseHelper.MD5(passwordMixed);
        sqlPara.put("id",userId);
        sqlPara.put("password", passwordMD5 );
        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement, sqlPara);
        }
        return user;
    }

    public Boolean updateUserName(String userId, String name) {
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null){
            return null;
        }
        Boolean result = true;
        String statement = "com.yangmz.user.model.userMapper.updateUserName";
        String checkStatement = "com.yangmz.user.model.userMapper.getUserByName";
        HashMap<String,String> sqlPara = new HashMap<String,String>();
        sqlPara.put("id",longUserId.toString());
        sqlPara.put("name", name );
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, sqlPara);
            session.commit();
            if (session.selectList(checkStatement, name).size() > 1) {
                session.rollback();
                result = false;
            }
        }
        return result;
    }

    /*
        type = [wechat| taobao| weibo]
     */
    public Boolean updateDetail(String userId, String type, String value) {
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null){
            return false;
        }
        if( type == null){
            return false;
        }
        if (!type.equals("wechat") && !type.equals("taobao") && !type.equals("weibo") ){
            return false;
        }

        String statement = "com.yangmz.user.model.userMapper.updateDetail";
        Map<String,Object> params = new HashMap<>();
        params.put("id",longUserId);
        params.put("type",type);
        params.put("value",value);
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, params);
            session.commit();
        }
        return true;
    }

    /*
        type = [wechat| taobao| weibo]
        value = [true| false]
    */
    public Boolean updateDetailValid(String userId, String type, String value) {
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null){
            return false;
        }
        if( type == null || value == null){
            return false;
        }
        if (!type.equals("wechat") && !type.equals("taobao") && !type.equals("weibo") ){
            return false;
        }

        String statement = "com.yangmz.user.model.userMapper.updateDetailValid";
        Map<String,Object> params = new HashMap<>();
        params.put("id",longUserId);
        params.put("type",type);
        if ( value.equals("true") ){
            params.put("value",1);
        }else {
            params.put("value",0);
        }
        try (SqlSession session = mybatisClient.openSession()) {
            session.update(statement, params);
            session.commit();
        }
        return true;
    }

    public User getUserById(String userId) {
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null){
            return null;
        }
        String statement = "com.yangmz.user.model.userMapper.getUserById";

        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement, longUserId );
        }
        return user;
    }

    public User getUserEmailById(String userId) {
        Long longUserId = BaseValue.getLongId(userId);
        if ( longUserId == null){
            return null;
        }
        String statement = "com.yangmz.user.model.userMapper.getUserEmailByID";

        User user = null;
        try (SqlSession session = mybatisClient.openSession()) {
            user = session.selectOne(statement, longUserId );
        }
        return user;
    }

    public List<User> getUserList(String userIdList) {
        List<Long> idList = new ArrayList<Long>();
        String[] stringIdList = userIdList.split("\\|",Env.ArticlePerPage+1);
        for (String id:stringIdList) {
            Long tempId = BaseValue.getLongId(id);
            if(tempId == null){
                break;
            }
            idList.add(tempId);
        }
        String statement = "com.yangmz.user.model.userMapper.getUserList";
        List<User> userList = null;
        try (SqlSession session = mybatisClient.openSession()) {
            userList = session.selectList(statement, idList );
        }
        return userList;
    }
}
