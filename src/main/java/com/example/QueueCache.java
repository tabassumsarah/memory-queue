package com.example;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

// This is a cache for storing queues as <Name,Queue>.
class QueueCache {

    private static QueueCache instance;
    private Cache<String, Object> cache;

    private QueueCache(int concurrencyLevel, int expiration, int size) throws IOException {
    // todo: all the properties should be taken from a properties file so that its configurable
        cache = CacheBuilder.newBuilder().concurrencyLevel(concurrencyLevel).maximumSize(size).softValues()
                .expireAfterWrite(expiration, TimeUnit.SECONDS).build();
    }

    static public synchronized QueueCache getInstance() throws IOException {
        if (instance == null) {
            // todo: make configurable
            instance = new QueueCache(10000, 3600,1000000);
        }
        return instance;
    }

    public Object get(String key) {
        ConcurrentMap<String,Object> map =cache.asMap();
        return map.get(key);
    }

    public void put(String key, Object obj) {
        cache.put(key, obj);
    }
}
