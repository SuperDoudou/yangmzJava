package com.yangmz.base.client.Jedis;

import java.util.Set;

public interface JedisClient {
    void connect();
    void disconnect();
    Boolean isConnected();

    String auth(String password);

    String set(String key, String value);
    /*
    when there is no key get null
     */
    String get(String key);
    Set<String> keys(String pattern);
    Long expire(String key, Integer second);
    Long expireMinute(String key, Integer minute);
}
