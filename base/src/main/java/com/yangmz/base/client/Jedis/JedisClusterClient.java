package com.yangmz.base.client.Jedis;

import java.util.Set;

public class JedisClusterClient implements JedisClient {

    public JedisClusterClient(String configPath) {
    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public Boolean isConnected() {
        return null;
    }

    @Override
    public String auth(String password) {
        return null;
    }

    @Override
    public String set(String key, String value) {
        return null;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public Long expire(String key, Integer second) {
        return null;
    }

    @Override
    public Long expireMinute(String key, Integer minute) {
        return null;
    }
}
