package com.pcz.permission.service;

import com.pcz.permission.beans.CacheKeyConstants;

/**
 * @author picongzhi
 */
public interface SysCacheService {
    void saveCache(String value, int timeout, CacheKeyConstants prefix);

    void saveCache(String value, int timeout, CacheKeyConstants prefix, String... keys);

    String getFromCache(CacheKeyConstants prefix, String... keys);
}
