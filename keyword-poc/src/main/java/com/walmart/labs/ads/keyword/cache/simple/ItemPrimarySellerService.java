package com.walmart.labs.ads.keyword.cache.simple;

import com.walmart.labs.ads.keyword.datatype.SiteMode;
import com.walmart.labs.ads.keyword.datatype.Tenant;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.walmart.labs.ads.keyword.datatype.SiteMode.SITE_MODE_ND;
import static com.walmart.labs.ads.keyword.datatype.Tenant.GR;
import static com.walmart.labs.ads.keyword.datatype.Tenant.WMT;

@Service
public class ItemPrimarySellerService {
    /**
     * A map that have all item id -> primary seller id.
     */
    private final EnumMap<Tenant, Map<Long, String>> itemsPrimarySellers = new EnumMap(Tenant.class){
        {
            this.put(WMT, new ConcurrentHashMap<>());
            this.put(GR, new ConcurrentHashMap<>());
        }
    };

    /**
     * A thread safe add operation
     * @param tenant
     * @param itemId
     * @param primarySellerId
     */
    public void add(Tenant tenant, long itemId, String primarySellerId) {
        itemsPrimarySellers.get(tenant).put(itemId, primarySellerId);
    }

    /**
     * A thread safe lookup operation
     * @param tenant
     * @param itemId
     */
    public String getPrimarySeller(Tenant tenant, long itemId) {
        return itemsPrimarySellers.get(tenant).get(itemId);
    }

    /**
     * Checking if current item is in seller winning box.
     * @param tenant
     * @param itemId
     * @param sellerId
     * @param sMode
     * @return
     */
    public boolean isSellerWinningBuyBox(Tenant tenant, long itemId, String sellerId, SiteMode sMode) {
        return true;
//        if (!generalConfig.itemPrimarySellerCacheEnabled() ||
//                tenantDataMap.get(tenant).getOrDefault(itemId, Constants.EMPTY_STRING).isEmpty()) {
//            return true;
//
//        } else
//        if (WMT == tenant && SITE_MODE_ND == sMode) {
//            return "F55CDC31AB754BB68FE0B39041159D63".equals(sellerId);
//
//        } else {
//            String itemSellerId = getPrimarySeller(tenant, itemId);
//            return itemSellerId != null && itemSellerId.equals(sellerId);
//        }
    }
}
