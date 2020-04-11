package com.walmart.labs.ads.keyword.cache.ehcache;

import com.walmart.labs.ads.keyword.Query;
import com.walmart.labs.ads.keyword.cache.KeywordLookupService;
import com.walmart.labs.ads.keyword.datatype.Tenant;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import com.walmart.labs.ads.keyword.serialization.ParsedKeywordBidSerializer;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.walmart.labs.ads.keyword.datatype.Tenant.GR;
import static com.walmart.labs.ads.keyword.datatype.Tenant.WMT;

/**
 * A two tier Cache system, first layer is a in-heap memory, second layer is off heap memory. Experiment on this can
 * let us to decide if we want to go off-heap solution in the future.
 */
public class MulltiTierKeywordLookupService implements KeywordLookupService {
    private static final Logger log = LoggerFactory.getLogger(MulltiTierKeywordLookupService.class);

    private static final ParsedKeywordBidSerializer commonSerializer = new ParsedKeywordBidSerializer();

    public MulltiTierKeywordLookupService(String persistentRoot) {
        this.tenantWordAdGroups =  new EnumMap(Tenant.class){{
            this.put(WMT, new EhcacheKeywordBidManager(1 << 16, WMT, persistentRoot));
            this.put(GR,  new EhcacheKeywordBidManager(1 << 16, GR, persistentRoot));
        }};
    }

    public static class EhcachedKeywordBidsSerializer implements Serializer<Set> {
        // This is needed for Ehcache.
        public EhcachedKeywordBidsSerializer(ClassLoader loader) {
        }

        @Override
        public ByteBuffer serialize(Set object) throws SerializerException {
            return ByteBuffer.wrap(commonSerializer.serialize(object));
        }

        @Override
        public Set read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
            return commonSerializer.deserialize(binary.array());
        }

        @Override
        public boolean equals(Set object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
            Set<ParsedKeywordBid> deserialized = read(binary);

            if (object.size() != deserialized.size())
                return false;

            for(ParsedKeywordBid parsedKeywordBid : deserialized) {
                if (!object.contains(parsedKeywordBid)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * A wrapper for Ehcache cache.
     */
     static class EhcacheKeywordBidManager {
        private final Cache cache;

        EhcacheKeywordBidManager(int heapCapacity, Tenant tenant, String storageRoot) {
            CacheConfigurationBuilder<Integer, Set> configBuilder = CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Integer.class, Set.class,
                            ResourcePoolsBuilder.heap(heapCapacity)
                                    .offheap(2, MemoryUnit.GB)
                                    .disk(40, MemoryUnit.GB, false));
            CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                    .with(CacheManagerBuilder.persistence(Paths.get(storageRoot, "keyword" +  tenant + ".data").toFile()))
                    .withCache("encache.keywordBid." + tenant.toString(), configBuilder)
                    .withSerializer(Set.class, EhcachedKeywordBidsSerializer.class)
                    .build();
            cacheManager.init();
            cache = cacheManager.getCache("encache.keywordBid." + tenant.toString(), Integer.class, Set.class);
        }

        public Set<ParsedKeywordBid> get(int adGroupId) {
            return (Set<ParsedKeywordBid>)cache.get(adGroupId);
        }

        public Set<ParsedKeywordBid> getOrDefault(int adGroupId, Set<ParsedKeywordBid> defaultValue) {
            if (cache.containsKey(adGroupId)) {
                return get(adGroupId);
            }
            return defaultValue;
        }

        public void put(int adGroupId, Set<ParsedKeywordBid> parsedKeywordBids) {
            cache.put(adGroupId, parsedKeywordBids);
        }

        public void putIfAbsent(int adGroupId, Set<ParsedKeywordBid> parsedKeywordBids) {
            cache.putIfAbsent(adGroupId, parsedKeywordBids);
        }
    }

    private final EnumMap<Tenant, EhcacheKeywordBidManager> tenantWordAdGroups;


    /**
     * Add ad group -> set OF ParsedKeywordBid into global map.
     * @param tenant
     * @param adGroupId
     * @param keywordBids
     */
    @Override
    public void addAdGroupKeywordBid(Tenant tenant, int adGroupId, Set<ParsedKeywordBid> keywordBids) {
        EhcacheKeywordBidManager manager = tenantWordAdGroups.get(tenant);
        manager.put(adGroupId, keywordBids);
    }

    /**
     *
     * Lookup with a query string along with a ad group id set.
     * @param tenant
     * @param searchQuery
     * @param adGroupIds
     * @return
     */
    @Override
    public Map<Integer, ParsedKeywordBid> queryMatching(Tenant tenant, String searchQuery, Set<Integer> adGroupIds) {
        if (searchQuery.trim().isEmpty())   return Collections.EMPTY_MAP;
        Query query = new Query(searchQuery);
        EhcacheKeywordBidManager manager = tenantWordAdGroups.get(tenant);
        Map<Integer, ParsedKeywordBid> bestBidsByAdGroup = new ConcurrentHashMap<>();
        adGroupIds.stream().forEach(adGroupId -> {
            Optional<ParsedKeywordBid> best = bestBid(query, manager.get(adGroupId));
            if (best.isPresent()) {
                bestBidsByAdGroup.put(adGroupId, best.get());
            }
        });

        return bestBidsByAdGroup;
    }

    /**
     * Simple search for keys.
     * @param tenant
     * @param adGroup
     * @return
     */
    @Override
    public Set<ParsedKeywordBid> search(Tenant tenant, int adGroup) {
        return tenantWordAdGroups.get(tenant).getOrDefault(adGroup, Collections.emptySet());
    }
}
