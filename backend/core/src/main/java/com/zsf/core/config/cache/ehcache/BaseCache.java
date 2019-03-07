package com.zsf.core.config.cache.ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author yyq
 */

@Component
@Scope(value = "prototype")
public class BaseCache {

    @Autowired
    private EhCacheCacheManager ehCacheCacheManager;

    private final String cacheName = "baseCache";

    private Cache cache = ehCacheCacheManager.getCache(cacheName);

    public Object get(Object key) {
        return cache.get(key);
    }

    public void set(Object key, Object value) {
        cache.put(key, value);
    }

    public void update(Object key, Object value) {
        cache.put(key, value);
    }

    public void remove(Object key) {
        cache.evict(key);
    }

    public void clear() {
        cache.clear();
    }
}
