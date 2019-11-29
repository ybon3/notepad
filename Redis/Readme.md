- [Redis 安装](http://www.runoob.com/redis/redis-install.html)

- [Redis 命令参考](http://redisdoc.com/)

- [官方完整指令](https://redis.io/commands)


在 Java 中使用
=============

- ~[Java 使用 Redis](http://www.runoob.com/redis/redis-java.html)~

- ~[SpringBoot学习（五）--SpringBoot集成redis缓存及redis客户端](https://blog.csdn.net/u011961421/article/details/79031784)~

- ~[Spring Boot中使用Redis数据库](http://blog.didispace.com/springbootredis/)~

- [关于redis，学会这8点就够了](https://blog.csdn.net/middleware2018/article/details/80355418)

- [Introduction to Spring Data Redis(CrudReposirtory)](https://www.baeldung.com/spring-data-redis-tutorial)

- [Using Redis with CrudRepository in Spring Boot](https://www.oodlestechnologies.com/blogs/Using-Redis-with-CrudRepository-in-Spring-Boot)

- [Redis數據結構與存儲](https://tw.saowen.com/a/7234a3b267eb736df86362b08108fcac98aa6f6665a02bd489c4f801aaf7a82c)

- [SpringBoot2.0+整合redis,使用 RedisTemplate操作redis](https://zhuanlan.zhihu.com/p/49078255)

- [serialization 的問題](https://stackoverflow.com/questions/28705921/ignore-transient-fields-of-an-entity-automatically-during-json-serialization)

- [Redis的數據保持時間問題，不設置expire是否永久有效？](https://kknews.cc/zh-tw/code/b4q5vkm.html)


Lettuce v.s Jedis
=================

在 `springboot 1.5.x` 版本的默认的 `Redis` 客户端是 `Jedis` 实现的，`springboot 2.x` 版本中默认客户端是用 `lettuce` 实现的。

- [springboot系列文章之 集成redis 服务 (Lettuce & Jedis)](https://juejin.im/post/5ba0a098f265da0adb30c684)

- [Spring boot - data-redis与jedis关系](https://www.jianshu.com/p/c7b4cd47ad65)



Example
=======

```java
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nkg.eshop.redis.repository.Cart;

@Configuration
@EnableCaching
public class RedisConfig {
	@Bean
	CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		//初始化一个RedisCacheWriter
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
		//设置默认超过期时间是1天
		//defaultCacheConfig.entryTtl(Duration.ofDays(1));
		defaultCacheConfig.entryTtl(Duration.ofMinutes(1L));
		//初始化RedisCacheManager
		RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
		return cacheManager;
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);

		template.setValueSerializer(serializer);
		//使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public RedisTemplate<String, Cart> cartRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Cart> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		Jackson2JsonRedisSerializer<Cart> serializer = new Jackson2JsonRedisSerializer<>(Cart.class);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    
    //避開那些非 field 的 Getter
		mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);

		template.setValueSerializer(serializer);
		//使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(factory);
		return stringRedisTemplate;
	}
}
```

我們也可以在要轉換的 class 中對特定的 method 加上 `@JsonIgnore` 來避免 serialization
