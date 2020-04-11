package com.walmart.labs.ads.keyword.cache.simple;

import com.walmart.labs.ads.keyword.datatype.*;
import com.walmart.labs.ads.keyword.util.KeyGenerator;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A service managed Placement Level Bid, it only provides under certain tenant, pageType, platform, moduleLocation and
 * taxonomy's cpc value.
 */
@Service
public class PlacementLevelBidService {
    /**
     * A CPC value when lookup failed.
     */
    private static final double FALLBACK_CPC = 0d;

    /**
     * A two level map for efficient lookup.
     * Fist level key is combination of Tenant, PageType, Platform, ModuleLocation.
     * Second level is map[taxonomy -> cpc]
     *
     */
    private final Map<Integer, Map<String, Double>> placementLevelBidMap = new ConcurrentHashMap<>();

    /**
     * A thread safe add placement level bid operation.
     * @param tenant
     * @param pt
     * @param plt
     * @param mLoc
     * @param taxonomy
     * @param cpc
     */
    public void add(Tenant tenant, PageType pt, Platform plt, ModuleLocation mLoc, String taxonomy, double cpc) {
        int firstLevelKey = KeyGenerator.placementLevelKey(tenant, pt, plt, mLoc);
        placementLevelBidMap.putIfAbsent(firstLevelKey, new ConcurrentHashMap<>());
        Map<String, Double> taxonomyCpc = placementLevelBidMap.get(firstLevelKey);
        taxonomyCpc.putIfAbsent(taxonomy, cpc);
    }


    /**
     * Lookup CPC by given tenant, pageType, platform, moduleLocation and taxonomy.
     * @param tenant
     * @param pt
     * @param plt
     * @param mLoc
     * @param taxonomy
     * @return
     */
    public double lookup(Tenant tenant, PageType pt, Platform plt, ModuleLocation mLoc, String taxonomy) {
        int firstLevelKey = KeyGenerator.placementLevelKey(tenant, pt, plt, mLoc);
        if (placementLevelBidMap.containsKey(firstLevelKey)) {
            Map<String, Double> taxonomyCpc = placementLevelBidMap.get(firstLevelKey);
            return taxonomyCpc.getOrDefault(taxonomy, FALLBACK_CPC);
        }
        return FALLBACK_CPC;
    }
}
