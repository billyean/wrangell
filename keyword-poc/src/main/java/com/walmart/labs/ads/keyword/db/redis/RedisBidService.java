package com.walmart.labs.ads.keyword.db.redis;

import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * This service is Jedis client for search/write ad group Keywords
 */
@Service
public class RedisBidService {
    private static final Logger log = LoggerFactory.getLogger(RedisBidService.class);

    private static final String HASH_KEY = "midas:adgroup:";
    /**
     *
     */
    @Autowired
    private RedisTemplate<String, Set<ParsedKeywordBid>> template;

    private HashOperations<String, String, Set<ParsedKeywordBid>> hashOperations;

    @PostConstruct
    public void init() {
        hashOperations = template.opsForHash();
    }

    public Set<ParsedKeywordBid> get(String key) {
        return hashOperations.get(HASH_KEY, key);
    }

    public void put(String key,  Set<ParsedKeywordBid> parsedKeywordBids) {
        hashOperations.put(HASH_KEY, key, parsedKeywordBids);
    }

    public List<Set<ParsedKeywordBid>> mget(Set<String> keys) {
        return hashOperations.multiGet(HASH_KEY, keys);
    }
}
