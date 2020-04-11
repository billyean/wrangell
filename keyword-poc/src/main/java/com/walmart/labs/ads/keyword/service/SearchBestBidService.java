package com.walmart.labs.ads.keyword.service;

import com.walmart.labs.ads.keyword.cache.KeywordLookupService;
import com.walmart.labs.ads.keyword.cache.simple.BlacklistedItemService;
import com.walmart.labs.ads.keyword.cache.simple.ItemCampaignService;
import com.walmart.labs.ads.keyword.cache.simple.ItemPrimarySellerService;
import com.walmart.labs.ads.keyword.config.CacheServiceFactory;
import com.walmart.labs.ads.keyword.datatype.SiteMode;
import com.walmart.labs.ads.keyword.datatype.Tenant;
import com.walmart.labs.ads.keyword.model.AdItemCampaign;
import com.walmart.labs.ads.keyword.model.MatchedBid;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import com.walmart.labs.ads.keyword.request.WMTBidRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Service
public class SearchBestBidService {
    private static final Logger log = LoggerFactory.getLogger(SearchBestBidService.class);

    @Autowired
    ItemCampaignService itemCampaignService;

    @Autowired
    CacheServiceFactory cacheServiceFactory;

    KeywordLookupService keywordLookupService;

    @Autowired
    ItemPrimarySellerService itemPrimarySellerService;

    @Autowired
    BlacklistedItemService blacklistedItemService;

    @PostConstruct
    public void init() {
        keywordLookupService = cacheServiceFactory.getService();
    }

    /**
     * Search the best key
     * @param request
     * @return
     */
    public Set<MatchedBid> search(WMTBidRequest request) {
        Tenant tenant = request.getTenant();
        double floorCPC = request.floorCPC();
        String searchQuery = request.getCorrectedNormalizedQuery();
        SiteMode sMode = request.getsMode();

        log.info("fllorCPC : {}", floorCPC);

        /**
         * Adapt dynamic item+campaign filter
         */
        BiPredicate<Long, AdItemCampaign> itemCampaignFilter = (item, adItemCampaign) -> {
            return itemPrimarySellerService.isSellerWinningBuyBox(tenant, item, adItemCampaign.getPangaeaSellerId(), sMode);
        };

        /**
         *  Adapt dynamic campaign filter
         */
        Predicate<AdItemCampaign> campaignFilter = (adItemCampaign) -> {
            return adItemCampaign.getCpc() >= floorCPC;
        };

        Stream<Long> items = request.getItemsStream().filter(blacklistedItemService::notBlacklisted);

        /**
         * Get all adGroup and Campaign map under evey given item id.Aedad
         */
        Map<Long, Map<Integer, AdItemCampaign>> itemGroupCampaigns = itemCampaignService.
                matchAdItemCampaigns(tenant, items, itemCampaignFilter, campaignFilter);

        /**
         * Deduplicate ad group ids for simplifying next search operation.
         */
        Set<Integer> adGroupIds = itemGroupCampaigns.values().stream().flatMap(v -> v.keySet().stream()).collect(toSet());

        /**
         * Find best keyword bid in every adgroup id.
         */
        Map<Integer, ParsedKeywordBid> bestBids = keywordLookupService.queryMatching(tenant, searchQuery, adGroupIds);

        bestBids.forEach((k, v) -> {
            log.info("adGroupId : {}, ParsedKeywordBid: {}", k, v);
        });

        /**
         * Calculate best keyword bid set by the max CPC keyword bid under every item id.
         */
        Set<MatchedBid> matchedBids = itemGroupCampaigns.entrySet().stream().map(itemGroupCampaign -> {
            long itemId = itemGroupCampaign.getKey();

            /**
             * Highest CPC wins, if CPC is same, exact match trumps phrase match and phrase match trump broad match.
              */
            Comparator<Map.Entry<Integer, AdItemCampaign>> comp =
                    Comparator.comparing((Map.Entry<Integer, AdItemCampaign> e) -> bestBids.get(e.getKey()).getCpc())
                        .thenComparing(e -> bestBids.get(e.getKey()).getMatchType().ordinal());
            Optional<MatchedBid> matchedBidOptional = itemGroupCampaign.getValue().entrySet().stream()
                    .filter(e -> bestBids.containsKey(e.getKey()) )
                    .max(comp).map(e -> new MatchedBid(itemId, e.getValue().getCampaignId(), e.getKey(),
                            bestBids.get(e.getKey()).getKeywords(), bestBids.get(e.getKey()).getMatchType(),
                            "", bestBids.get(e.getKey()).getCpc(), e.getValue().getPangaeaSellerId()));
            return matchedBidOptional;
        }).filter(Optional::isPresent).map(Optional::get).collect(toSet());

        return matchedBids;
    }
}
