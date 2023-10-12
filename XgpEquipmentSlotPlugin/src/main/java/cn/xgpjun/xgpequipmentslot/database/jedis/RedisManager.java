package cn.xgpjun.xgpequipmentslot.database.jedis;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;

public class RedisManager {
    private final JedisPool redisPool;
    public RedisManager(){
        File configFile = new File(XgpEquipmentSlot.getInstance().getDataFolder(), "database.yml");
        FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);
        String host = dataConfig.getString("redis.host","localhost");
        int port = dataConfig.getInt("redis.port",6379);
        String password = dataConfig.getString("redis.password","");
        int timeout = dataConfig.getInt("redis.timeout",5000);
        int database = dataConfig.getInt("redis.database",0);
        String clientName = dataConfig.getString("redis.clientName","XES_client");
        int maxTotal = dataConfig.getInt("redis.maxTotal",50);
        int maxIdle = dataConfig.getInt("redis.maxIdle",10);
        int minIdle = dataConfig.getInt("redis.minIdle",5);

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        if(password.equals(""))
            password =null;
        redisPool = new JedisPool(config,host,port,timeout,password,database,clientName);
    }
    public Jedis getJedis(){
        return redisPool.getResource();
    }

    public void close(){
        redisPool.close();
    }
}
