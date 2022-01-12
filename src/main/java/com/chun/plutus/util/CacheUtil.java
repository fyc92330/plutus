package com.chun.plutus.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CacheUtil implements InitializingBean {

  @Autowired
  private RedisCacheManager cacheManager;
  @Autowired
  private RedisTemplate<String, Object> redisTemplate; //redis的操作封裝物件

  /** ============ bean 初始化  ============ */
  @Override
  public void afterPropertiesSet() throws Exception {
  }


  /**
   * 取得指定cacheName的cache data
   */
  public Map<String, Object> getCacheData(String cacheName) {
    return getRedisCacheData(cacheName);
  }

  /**
   * 清除指定cacheName的cache
   *
   * @param cacheName
   * @return
   */
  public void clearCache(String cacheName) {
    Cache cache = getCache(cacheName);
    if (cache != null) {
      cache.clear();
    }
  }

  /**============ 單一 cache 的 cacheKey 操作  ============*/

  /**
   * @param cacheName
   * @param cacheKey
   */
  public <T> T getObjectFromCache(String cacheName, String cacheKey) {
    Cache cache = getCache(cacheName);
    Cache.ValueWrapper ele = cache.get(cacheKey);
    return ele != null ? (T) ele.get() : null;
  }

  /**
   * @param cacheName
   * @param cacheKey
   * @param obj
   */
  public void putIntoCache(String cacheName, String cacheKey, Object obj) {
    Cache cache = getCache(cacheName);
    cache.put(cacheKey, obj);
  }

  /**
   * @param cacheName
   * @param cacheKey
   */
  public void removeCache(String cacheName, String cacheKey) {
    Cache cache = getCache(cacheName);
    cache.evict(cacheKey);
  }

//  public void removeCacheKey(String cacheName, String cacheKey) {
//    String key = StringUtil.concat(cacheName.split(Const.UNDERLINE_STR)[0], Const.COLON_STR, cacheKey);
//    redisTemplate.delete(key);
//  }

  /**
   * 取得指定cacheName的cache
   *
   * @param cacheName
   */
  private Cache getCache(String cacheName) {
    return cacheManager.getCache(cacheName);
  }
  /**============ Redis cache 操作 ============*/

  /**
   * 取得Redis指定cacheName的cache資料
   *
   * @param cacheName
   * @return
   */
  private Map<String, Object> getRedisCacheData(String cacheName) {
    Map<String, Object> cacheData = new HashMap<>();
    for (String key : redisTemplate.keys(cacheName.concat(":*"))) {
      cacheData.put(
          StringUtils.replaceOnce(key, cacheName.concat(":"), ""),
          redisTemplate.opsForValue().get(key)
      );
    }
    return cacheData;
  }
}
