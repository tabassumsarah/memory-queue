package com.example;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

class MessageCache {

    private static MessageCache instance;
    Cache<String, Object> cache;

    private MessageCache(int concurrencyLevel, int expiration, int size) throws IOException {

        cache = CacheBuilder.newBuilder().concurrencyLevel(concurrencyLevel).maximumSize(size).softValues()
                .expireAfterWrite(expiration, TimeUnit.SECONDS).build();
    }
   //todo: Make it configurable (properties file)
    static public synchronized MessageCache getInstance() throws IOException {
        if (instance == null) {
            instance = new MessageCache(10000, 3600,1000000);
        }
        return instance;
    }

    Cache getCache(){
        return  cache;
    }

    public Object get(String key) {
        ConcurrentMap<String,Object> map =cache.asMap();
        return map.get(key);
    }

    public void put(String key, Object obj) {
        cache.put(key, obj);
    }

    public void delete(String key, Object obj) {
        cache.invalidate(obj);
    }
}
