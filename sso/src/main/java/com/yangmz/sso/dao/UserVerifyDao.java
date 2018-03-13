package com.yangmz.sso.dao;

import com.yangmz.base.client.Jedis.JedisClient;
import com.yangmz.base.client.Jedis.JedisFactory;
import com.yangmz.base.helper.BaseHelper;
import org.springframework.stereotype.Service;

/*
登陆验证码
 */
@Service
public class UserVerifyDao {
    JedisClient jedisClient = null;
    private final String TABLE = "loginUserVerify:";
    UserVerifyDao(JedisFactory jedisFactory) {
        jedisClient = jedisFactory.getClient();
    }
    /*
    设置Email 的验证码
    key: loginUserVerify:email:{email}:code
    value: {code}
     */
    private String generateEmailCodeKey(String email) {
        return TABLE + "email:" + email + ":code";
    }

    public String setEmailVerifyCode(String email) {
        if (!BaseHelper.isValidEmail(email)){
            return null;
        }
        String code = BaseHelper.generateVerifyCode();
        String key = generateEmailCodeKey(email);
        jedisClient.set(key, code);
        jedisClient.expireMinute(key, 10);
        return code;
    }

    public String getEmailVerifyCode(String email) {
        if (!BaseHelper.isValidEmail(email)){
            return "";
        }
        String key = generateEmailCodeKey(email);
        return jedisClient.get(key);
    }

    public Boolean checkEmailVerifyCode(String email, String code) {
        if (code == null || code.length() == 0
                || !BaseHelper.isValidEmail(email)){
            return false;
        }
        String codeData = getEmailVerifyCode(email);
        return code.equals(codeData);
    }
}
