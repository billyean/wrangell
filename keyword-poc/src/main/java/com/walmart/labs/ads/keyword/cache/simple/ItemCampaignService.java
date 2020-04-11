package com.walmart.labs.ads.keyword.cache.simple;

import com.walmart.labs.ads.keyword.datatype.Tenant;
import com.walmart.labs.ads.keyword.model.AdItemCampaign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toMap;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.walmart.labs.ads.keyword.datatype.Tenant.GR;
import static com.walmart.labs.ads.keyword.datatype.Tenant.WMT;
import static com.walmart.labs.ads.keyword.model.AdItemCampaign.FALLBACK_ADITEM_CAMPAIGN;


/**
 * A Item Campaign Service that has ruled out all blacklisted items.
 */
@Service
public class ItemCampaignService {
    private static final Logger log = LoggerFactory.getLogger(ItemCampaignService.class);

    /**
     * A placeholder for helping remove thread safe.
     */
    private static final Map<Long, AdItemCampaign> EMPTY = new ConcurrentHashMap<>();

    /**
     * A two level map for efficient lookup.
     * Fist level key is item id.
     * Second level is map[ad group id -> AdItemCampaign]
     *
     */
    private final EnumMap<Tenant, Map<Long, Map<Integer, AdItemCampaign>>> itemCampaignMaps = new EnumMap(Tenant.class){
        {
            this.put(WMT, new ConcurrentHashMap<>());
            this.put(GR, new ConcurrentHashMap<>());
        }
    };

    /**
     * A thread safe add placement level bid operation.
     */
    public void add(Tenant tenant, long itemId, int adGroupId, AdItemCampaign campaign) {
        Map<Long, Map<Integer, AdItemCampaign>>itemCampaignMap = itemCampaignMaps.get(tenant);
        itemCampaignMap.putIfAbsent(itemId, new ConcurrentHashMap<>());
        Map<Integer, AdItemCampaign> adGroupCampaign = itemCampaignMap.get(itemId);
        adGroupCampaign.put(adGroupId, campaign);
    }

    /**
     * A thread safe add placement level bid operation.
     */
    public AdItemCampaign lookup(Tenant tenant, long itemId, int adGroupId) {
        Map<Long, Map<Integer, AdItemCampaign>>itemCampaignMap = itemCampaignMaps.get(tenant);
        if (itemCampaignMap.containsKey(itemId)) {
            Map<Integer, AdItemCampaign> adGroupCampaign = itemCampaignMap.get(itemId);
            return adGroupCampaign.getOrDefault(adGroupId, FALLBACK_ADITEM_CAMPAIGN);
        }
        return FALLBACK_ADITEM_CAMPAIGN;
    }

    /**
     * A thread safe remove operation.
     * @param itemId
     * @param adGroupId
     */
    public void remove(Tenant tenant, long itemId, int adGroupId) {
        Map<Long, Map<Integer, AdItemCampaign>>itemCampaignMap = itemCampaignMaps.get(tenant);
        Map<Integer, AdItemCampaign> adGroupCampaign = itemCampaignMap.get(itemId);
        if (adGroupCampaign != null) {
            adGroupCampaign.remove(adGroupId);
        }
        if (adGroupCampaign.isEmpty()) {
            itemCampaignMap.remove(itemId, EMPTY);
        }
    }

    /**
     * Search all item's corresponding adgroup and campaign, removed any campaign that is not satisfied by given
     * compaignFilter.
     * @param tenant
     * @param items
     * @return
     */
    public Map<Long, Map<Integer, AdItemCampaign>> matchAdItemCampaigns(Tenant tenant, Stream<Long> items,
                                                                        BiPredicate<Long, AdItemCampaign> itemCompaignFilter,
                                                                        Predicate<AdItemCampaign> compaignFilter) {
        Map<Long, Map<Integer, AdItemCampaign>> itemCampaignMap = itemCampaignMaps.get(tenant);

        Map<Long, Map<Integer, AdItemCampaign>> result = new HashMap<>();
        items.filter(itemCampaignMap::containsKey).forEach(item -> {
            Map<Integer, AdItemCampaign> adgroupCampaigns = itemCampaignMap.get(item).entrySet().stream().
                    filter(e -> itemCompaignFilter.test(item, e.getValue())).
                    filter(e -> compaignFilter.test(e.getValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (!adgroupCampaigns.isEmpty()) {
                result.put(item, adgroupCampaigns);
            }
        });
        return result;
    }
}
