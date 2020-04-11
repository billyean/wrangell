package com.walmart.labs.ads.keyword.config;

import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import com.walmart.labs.ads.keyword.serialization.ParsedKeywordBidSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Set;

/**
 * Custom RedisTemplate for handling read Map[AdGroupId, Set<ParsedKeywordBid>>.
 * Here there're assumption of adgroup key will be defined as
 * midas:adg:[adgroup id]
 * This template support custom key / value deserializer.
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    private @Value("${spring.redis.host}") String redisHost;
    private @Value("${spring.redis.port}") int redisPort;
    private @Value("${spring.redis.password}") String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().build();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setPassword(redisPassword);
        redisStandaloneConfiguration.setHostName(redisHost);
        RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Set<ParsedKeywordBid>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Set<ParsedKeywordBid>> redisTemplate = new RedisTemplate<>();

        ParsedKeywordBidSerializer valueSerializer = new ParsedKeywordBidSerializer();

        StringRedisSerializer adGroupIdSerializer = new StringRedisSerializer();

        /**
         * Current key is just tenant+ad group id, for the simplicity, string representation is used right now.
         * We can change it as below more concise Base64 encoded key if necessary.
         */
//        KeywordBidKeySerializer keySerializer = new KeywordBidKeySerializer();
//
//        RedisSerializer adGroupIdSerializer = new RedisSerializer<Integer>() {
//            @Override
//            public byte[] serialize(Integer adgroupId) throws SerializationException {
//                return keySerializer.serialize(adgroupId);
//            }
//
//            @Override
//            public Integer deserialize(byte[] bytes) throws SerializationException {
//                return keySerializer.deserialize(bytes);
//            }
//        };


        RedisSerializer keywordBidSerializer = new RedisSerializer<Set<ParsedKeywordBid>>() {
            @Override
            public byte[] serialize(Set<ParsedKeywordBid> parsedKeywordBids) throws SerializationException {
                return valueSerializer.serialize(parsedKeywordBids);
            }

            @Override
            public Set<ParsedKeywordBid> deserialize(byte[] bytes) throws SerializationException {
                return valueSerializer.deserialize(bytes);
            }
        };
        redisTemplate.setKeySerializer(adGroupIdSerializer);
        redisTemplate.setValueSerializer(keywordBidSerializer);
        redisTemplate.setHashKeySerializer(adGroupIdSerializer);
        redisTemplate.setHashValueSerializer(keywordBidSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
