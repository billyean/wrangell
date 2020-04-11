package com.walmart.labs.ads.keyword.request;

import com.walmart.ads.perceive.normalizer.QueryNormalizer;
import com.walmart.labs.ads.keyword.datatype.*;
import com.walmart.labs.ads.keyword.cache.simple.PlacementLevelBidService;
import com.walmart.labs.ads.keyword.util.KeyGenerator;
import com.walmart.labs.ads.keyword.util.Utilities;
import com.walmart.labs.ads.keyword.validator.EnumPattern;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import static com.walmart.labs.ads.keyword.datatype.SiteMode.SITE_MODE_DEFAULT;

public class BasedBidRequest {

    private static final QueryNormalizer queryNormalizer = new QueryNormalizer();

    public String getRequestId() {
        return requestId;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public PageType getPageType() {
        return pageType;
    }

    public Platform getPlatform() {
        return platform;
    }

    public ModuleLocation getmLoc() {
        return mLoc;
    }

    public SiteMode getsMode() {
        return sMode;
    }

    public String getShelfId() {
        return shelfId;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getPageId() {
        return pageId;
    }

    public String getNormalizedQuery() {
        return normalizedQuery;
    }

    public String getQuery() {
        return query;
    }

    public String getPlacementId() {
        return placementId;
    }

    public String getCorrectedNormalizedQuery() {
        return query.length() != 0 ?
                queryNormalizer.normalize(query) :
                normalizedQuery;
    }

    /**
     * It's session id like to maintain the response matching sent request.
     */
    @NotEmpty(message = "Please provide a request id")
    String requestId;

    /**
     * @see com.walmart.labs.ads.keyword.datatype.Tenant
     */
    @EnumPattern(regexp = "WMT|GR")
    private Tenant tenant;

    /**
     * @see com.walmart.labs.ads.keyword.datatype.PageType
     */
    @EnumPattern(regexp = "SEARCH|BROWSE|CATEGORY|STOCKUP|ITEM")
    private PageType pageType;

    /**
     * @see com.walmart.labs.ads.keyword.datatype.Platform
     */
    @EnumPattern(regexp = "MOBILE|DESKTOP|APP")
    private Platform platform;

    /**
     * @see com.walmart.labs.ads.keyword.datatype.ModuleLocation
     */
    @EnumPattern(regexp = "TOP|MIDDLE|BOTTOM")
    private ModuleLocation mLoc;

    /**
     * @see com.walmart.labs.ads.keyword.datatype.SiteMode
     */
    SiteMode sMode = SITE_MODE_DEFAULT;

    String shelfId;

    /**
     * Categories
     */
    List<String> categories = new ArrayList<>();

    String storeId = "";

    String pageId = "";

    String normalizedQuery = "";

    @NotEmpty(message = "Expect a query")
    String query;

    String placementId = "";

    @Autowired
    PlacementLevelBidService placementLevelBidService;

    /**
     * Find floor CPC by given category, the floor CPC will prevent the low CPC bid.
     * @return
     */
    public double floorCPC() {
//        int firstLevelKey = KeyGenerator.placementLevelKey(tenant, pageType, platform, mLoc);
        OptionalDouble floorCpc = categories.stream().map(Utilities::parseRequestCategory).mapToDouble(category ->
                placementLevelBidService.lookup(tenant, pageType, platform, mLoc, category)).min();
        return floorCpc.orElse(0.0d);
    }

}
