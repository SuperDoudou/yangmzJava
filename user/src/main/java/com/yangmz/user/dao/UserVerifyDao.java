package com.yangmz.user.dao;

import com.yangmz.base.client.Jedis.JedisClient;
import com.yangmz.base.client.Jedis.JedisFactory;
import com.yangmz.base.helper.BaseHelper;
import com.yangmz.base.helper.BaseValue;
import org.springframework.stereotype.Service;

/*
重置密码验证码
 */
@Service
public class UserVerifyDao {
    JedisClient jedisClient = null;
    private final String TABLE = "resetPasswordVerify:";
    UserVerifyDao(JedisFactory jedisFactory) {
        jedisClient = jedisFactory.getClient();
    }
    /*
    设置Email 的验证码
    key: resetPasswordVerify:userId:{email}:code
    value: {code}
     */
    private String generateEmailCodeKey(String userId) {
        return TABLE + "userId:" + userId + ":code";
    }

    public String setResetVerifyCode(String userId) {
        Long longUserId = BaseValue.getLongId(userId);
        if (longUserId == null) {
            return null;
        }
        String code = BaseHelper.generateVerifyCode();
        String key = generateEmailCodeKey(userId);
        jedisClient.set(key, code);
        jedisClient.expireMinute(key, 10);

        return code;
    }

    public String getResetVerifyCode(String userId) {
        Long longUserId = BaseValue.getLongId(userId);
        if (longUserId == null) {
            return "";
        }
        String key = generateEmailCodeKey(userId);
        return jedisClient.get(key);
    }

    public Boolean checkResetVerifyCode(String userId, String code) {
        Long longUserId = BaseValue.getLongId(userId);
        if (code == null || code.length() == 0
                ||longUserId == null ){
            return false;
        }
        String codeData = getResetVerifyCode(userId);
        return code.equals(codeData);
    }
}
