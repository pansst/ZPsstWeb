package com.psst.test;

import java.util.UUID;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.psst.common.util.RedisUtil;

public class TestRedis {
    private static final String THE_KEY = "ceshi_123";
    private static final String[] NAMES = {"张三","李四","王麻子","搜索","悟空"};
    public static void clearCache(){
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            jedis.del(THE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            RedisUtil.returnResource(jedis);
        }
    }
    public static void randomData(){
        clearCache();
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            for(int index = 0; index < 100; index++) {
                String uuid = UUID.randomUUID().toString();
                StoreData data = new StoreData();
                data.setUuid(uuid);
                data.setName(NAMES[(int)(Math.random() * NAMES.length)]);
                data.setReson("我想试一试" + index);
                //jedis.rpush(THE_KEY, JSON.toJSONString(data));
                Long dd = jedis.lpush(THE_KEY, JSON.toJSONString(data));
                System.out.println(dd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            RedisUtil.returnResource(jedis);
        } 
    }
    public static void main(String[] args) {
        randomData();
//        Jedis jedis = null;
//        try {
//            jedis = RedisUtil.getJedis();
//            String dataStr = jedis.lindex(THE_KEY, 10);
//            System.out.println(dataStr);
//            //StoreData data = JSON.parseObject(dataStr, StoreData.class);
//            JSONObject object = JSON.parseObject(dataStr);
//            //data.setName("加油，青春");
//            //jedis.lset(THE_KEY, 10, JSON.toJSONString(data));
//           // dataStr = jedis.lindex(THE_KEY, 10);
//            System.out.println(object.toJSONString());
//            //System.out.println(dataStr);
//            
//           /* jedis.rpush(THE_KEY, JSON.toJSONString(data));
//            List<String> list = jedis.lrange(THE_KEY, 0, -1);
//            System.out.println("数目:" + list.size());
//            System.out.println();*/
//            System.out.println("数目:" +jedis.llen(THE_KEY));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            RedisUtil.returnResource(jedis);
//        }
    }
}
