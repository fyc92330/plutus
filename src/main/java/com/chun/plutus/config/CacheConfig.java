package com.chun.plutus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

  @Override
  public KeyGenerator keyGenerator() {
    return new KeyGenerator() {
      /**
       * 預設的keyGenerator for @Cacheable/@CacheEvict/@CachePut/@CacheConfig
       * 由於usePrefix的設定，使得key會在前面加上CacheName:
       *
       * @param target
       * @param method
       * @param params
       * @return
       */
      @Override
      public Object generate(Object target, Method method, Object... params) {
        return String.format("%s.%s:%s", method.getDeclaringClass().getSimpleName(), method.getName(),
            Arrays.stream(params).map(Objects::toString).map(Objects::hashCode).map(Objects::toString).collect(Collectors.joining()));
      }
    };
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

    RedisCacheConfiguration cacheDefaults = getRedisCacheConfiguration(Duration.ofDays(7L));
    return new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory), cacheDefaults);
  }

  private RedisCacheConfiguration getRedisCacheConfiguration(Duration ttl) {
    CacheKeyPrefix cacheKeyPrefix = cacheName -> cacheName.concat(":");
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
        .entryTtl(ttl)
        .computePrefixWith(cacheKeyPrefix);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    /** redis序列化預設使用 JdkSerializationRedisSerializer ，prefix會帶\xac\xed\x00\x05t\x00。改用Json序列化 **/
    redisTemplate.setKeySerializer(keySerializer());
    redisTemplate.setConnectionFactory(connectionFactory);
    return redisTemplate;
  }

  private RedisSerializer<String> keySerializer() {
    return new StringRedisSerializer();
  }

  private RedisSerializer<Object> valueSerializer() {
    return new JdkSerializationRedisSerializer();
  }
}
