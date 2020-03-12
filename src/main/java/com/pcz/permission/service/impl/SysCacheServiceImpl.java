package com.pcz.permission.service.impl;

import com.google.common.base.Joiner;
import com.pcz.permission.beans.CacheKeyConstants;
import com.pcz.permission.service.SysCacheService;
import com.pcz.permission.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

/**
 * @author picongzhi
 */
@Service
@Slf4j
public class SysCacheServiceImpl implements SysCacheService {
    @Autowired
    private RedisPool redisPool;

    @Override
    public void saveCache(String value, int timeout, CacheKeyConstants prefix) {
        saveCache(value, timeout, prefix, null);
    }

    @Override
    public void saveCache(String value, int timeout, CacheKeyConstants prefix, String... keys) {
        if (value == null) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeout, value);
        } catch (Exception e) {
            log.error("save cache exception, prefix: {}, keys: {}, e: {}",
                    prefix.name(), JsonMapper.objectToString(keys), e);
        } finally {
            redisPool.saveClose(shardedJedis);
        }
    }

    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }

        return key;
    }

    @Override
    public String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            return shardedJedis.get(cacheKey);
        } catch (Exception e) {
            log.error("get cache exception, prefix: {}, keys: {}, e: {}",
                    prefix.name(), JsonMapper.objectToString(keys), e);
            return null;
        } finally {
            redisPool.saveClose(shardedJedis);
        }
    }
}
