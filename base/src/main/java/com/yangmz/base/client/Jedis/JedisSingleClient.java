package com.yangmz.base.client.Jedis;

import com.yangmz.base.helper.BaseValue;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.Properties;
import java.util.Set;

public class JedisSingleClient implements JedisClient {

    private String host;
    private Integer port;
    private String password;
    private Jedis jedis;
    private JedisSingleClient instant;

    private static Logger logger = Logger.getLogger(JedisSingleClient.class);

    JedisSingleClient(String configPath) {
        if (configPath == null ){
            return;
        }
        Properties properties = new Properties();
        String filePath = this.getClass().getResource(configPath).getPath();
        File file = new File(filePath);
        try(FileReader fileReader = new FileReader(file);
            BufferedReader read = new BufferedReader(fileReader) ){
            properties.load(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
        host = properties.getProperty("Host");
        port = Integer.valueOf(properties.getProperty("Port"));
        password = properties.getProperty("Password");
        logger.info("host is " + host + ", port is " + port);
        jedis = new Jedis(host, port);
        jedis.auth(password);
    }

    @Override
    public void connect(){
        jedis.auth(password);
        jedis.connect();
    }
    @Override
    public void disconnect(){
        jedis.disconnect();
    }
    @Override
    public Boolean isConnected() {
        return jedis.isConnected();
    }
    @Override
    public String auth(String password) {
        return jedis.auth(password);
    }
    @Override
    public String set(String key, String value){
        return jedis.set(key, value);
    }
    @Override
    public String get(String key){
        return jedis.get(key);
    }
    @Override
    public Set<String> keys(String pattern){
        return jedis.keys(pattern);
    }

    @Override
    public Long expire(String key, Integer second) {
        return jedis.expire(key, second);
    }

    @Override
    public Long expireMinute(String key, Integer minute) {
        return jedis.expire(key, minute * 60);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}