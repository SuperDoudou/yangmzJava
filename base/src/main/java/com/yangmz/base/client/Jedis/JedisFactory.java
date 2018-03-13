package com.yangmz.base.client.Jedis;

import java.util.HashMap;
import java.util.Map;

public class JedisFactory {

    Map<String,JedisClient> jedisClientMap;
    /*
    type = [single | cluster]
     */
    JedisFactory(String type, String configPath) {
        if ( type == null ){
            return;
        }
        JedisClient jedisClient = newJedisClient(type, configPath);
        jedisClientMap = new HashMap<>();
        jedisClientMap.put("default",jedisClient);
    }

    /*
    新建一个client加入Map
     */
    public Boolean createClient(String type, String configPath, String key) {
        if ( key == null || key.length() == 0 || jedisClientMap.containsKey(key) ){
            return false;
        }
        JedisClient jedisClient = newJedisClient(type, configPath);
        jedisClientMap.put(key,jedisClient);
        return true;
    }

    /*
    新建client
     */
     private JedisClient newJedisClient(String type, String configPath) {
         JedisClient jedisClient = null;
         if ( type.equals("single")){
             jedisClient = new JedisSingleClient(configPath);
         }
         if ( type.equals("cluster")){
             jedisClient = new JedisClusterClient(configPath);
         }
         return jedisClient;
     }

    public JedisClient getClient() {
        return jedisClientMap.get("default");
    }

}
