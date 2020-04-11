package com.walmart.labs.ads.keyword.db.mysql;

import com.walmart.labs.ads.keyword.cache.KeywordLookupService;
import com.walmart.labs.ads.keyword.cache.simple.*;
import com.walmart.labs.ads.keyword.config.CacheServiceFactory;
import com.walmart.labs.ads.keyword.datatype.*;
import com.walmart.labs.ads.keyword.model.AdItemCampaign;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import com.walmart.labs.ads.keyword.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MySQLBidService {
    private static final Logger log = LoggerFactory.getLogger(MySQLBidService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CacheServiceFactory cacheServiceFactory;

    KeywordLookupService keywordLookupService;

    @Autowired
    BlacklistedItemService blacklistedItemService;

    @Autowired
    PlacementLevelBidService placementLevelBidService;

    @Autowired
    ItemCampaignService itemCampaignService;

    @Autowired
    ItemPrimarySellerService itemPrimarySellerService;

    @PostConstruct
    public void init() {
        keywordLookupService = cacheServiceFactory.getService();
    }

    /**
     * A simple implementation reading data to build KeywordLookupService.
     */
    public void fullReadAdGroupKeywords() {
        final AtomicInteger counter = new AtomicInteger(1);
        jdbcTemplate.query("SELECT ad_group_id, tenant, white_listed_keyword_bids, black_listed_keyword_bids, " +
                "category_bids FROM ad_group_keywords WHERE is_active = 'Y' and white_listed_keyword_bids is not null",
                (rs) -> {
            int adGroup = rs.getInt("ad_group_id");
            Tenant tenant = Tenant.valueOf(rs.getString("tenant"));
            String whileListedKeywordBids = rs.getString("white_listed_keyword_bids");
            try {
                keywordLookupService.addAdGroupKeywordBid(tenant, adGroup, ParsedKeywordBid.parseJson(whileListedKeywordBids));
                counter.incrementAndGet();
            } catch (Exception ex) {
                log.error("Exception: {}.",  ex);
            }
        });
        log.info("Loaded ad_group_keywords table, total loaded {} rows.", counter.get());
    }

    /**
     * A simple implementation reading data to build blacklisted item service.
     */
    public void fullReadBlackedList() {
        final AtomicInteger counter = new AtomicInteger(1);
        jdbcTemplate.query("SELECT item_id AS blacklisted_id FROM blacklisted_item WHERE is_active = 'Y'",
                (rs) -> {
                    blacklistedItemService.add(rs.getLong("blacklisted_id"));
                    counter.incrementAndGet();
                });
        log.info("Loaded blacklisted_item table, total loaded {} rows.", counter.get());
    }

    /**
     * A simple implementation reading data to build placement level bid service.
     */
    public void fullReadPlacementLevelBid() {
        final AtomicInteger counter = new AtomicInteger(1);
        jdbcTemplate.query("SELECT tenant, page_type, platform, module_location, taxonomy, cpc " +
                        "FROM placement_level_bid WHERE is_active = 'Y'",
                (rs) -> {
                    Tenant tenant = Tenant.valueOf(rs.getString("tenant").toUpperCase());
                    PageType pt = PageType.valueOf(rs.getString("page_type").toUpperCase());
                    Platform plt = Platform.valueOf(rs.getString("platform").toUpperCase());
                    ModuleLocation mLoc = ModuleLocation.valueOf(rs.getString("module_location").toUpperCase());
                    String taxonomy = Utilities.normalize(rs.getString("taxonomy"));
                    double cpc = rs.getDouble("cpc");
                    placementLevelBidService.add(tenant, pt, plt, mLoc, taxonomy, cpc);
                    counter.incrementAndGet();
                });
        log.info("Loaded placement_level_bid table, total loaded {} rows.", counter.get());
    }

    /**
     * A simple implementation reading data to build placement level bid service.
     */
    public void fullReadAdItemCampaign() {
        final AtomicInteger counter = new AtomicInteger(1);
        jdbcTemplate.query("select tenant, item_id, campaign_id, ad_group_id, campaign_type, cpc, cpc_map, " +
                        "discount, pangaea_seller_id from ad_item_campaign n where exists (select * from ad_item_catalog g " +
                        "where g.tenant = n.tenant and g.item_id = n.item_id and g.is_active = 'Y')",
                (rs) -> {
                    long itemId = rs.getLong("item_id");
                    /**
                     * Temp solution to filter blacklisted item. Ideally Database operation should be business logic
                     * dependant.
                     */
                    if (blacklistedItemService.notBlacklisted(itemId)) {
                        Tenant tenant = Tenant.valueOf(rs.getString("tenant").toUpperCase());
                        int adGroupId = rs.getInt("ad_group_id");
                        int campaignId = rs.getInt("campaign_id");
                        CampaignType ct = CampaignType.valueOf(rs.getString("campaign_type").toUpperCase());
                        double cpc = rs.getDouble("cpc");
                        Map<Integer, Double> cpcMap = Utilities.parseCPCMap(rs.getString("cpc_map"));
                        double discount = rs.getDouble("discount");
                        String pangaeaSellerId = rs.getString("pangaea_seller_id");
                        AdItemCampaign adItemCampaign = new AdItemCampaign(itemId, campaignId, adGroupId, ct, cpc, cpcMap
                                ,discount,pangaeaSellerId);
                        itemCampaignService.add(tenant, itemId, adGroupId, adItemCampaign);
                        counter.incrementAndGet();
                    }
                });
        log.info("Loaded ad_item_campaign table, total loaded {} rows.", counter.get());
    }

    /**
     * A simple implementation reading data to build item primary seller service.
     */
    public void fullReadItemPrimarySellers() {
        final AtomicInteger counter = new AtomicInteger(0);
        jdbcTemplate.query("select tenant, item_id, primary_seller_id from item_primary_seller",
                (rs) -> {
                    long itemId = rs.getLong("item_id");
                    if (blacklistedItemService.notBlacklisted(itemId)) {
                        Tenant tenant = Tenant.valueOf(rs.getString("tenant").toUpperCase());
                        itemPrimarySellerService.add(tenant, itemId, rs.getString("primary_seller_id"));
                        counter.incrementAndGet();
                    }
                });
        log.info("Loaded item_primary_seller table, total loaded {} rows.", counter.get());
    }
}
