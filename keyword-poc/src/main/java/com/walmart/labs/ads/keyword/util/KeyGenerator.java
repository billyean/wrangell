package com.walmart.labs.ads.keyword.util;

import com.walmart.labs.ads.keyword.datatype.*;

public class KeyGenerator {
    /**
     * Generate a key for enumeration. The purpose for this is replaced all enumeration string with one int, which can
     * save (N * 2 - 4) bytes assume N is the total number of enumeration's String representation.
     * Although combination here is just 8 bits, short seems better choice here, but fact is in Java, padding will be
     * used as single key, int is the reasonable choice here.
     *
     * @param tenant [GR/WMT] -> 1 bits
     * @param pt     [SEARCH/BROWSE/CATEGORY/ITEM/STOCKUP] -> 3 bites
     * @param plt    [MOBILE/APP/BROWSER] -> 2 bits
     * @param mLoc   [TOP/MIDDLE/BOTTOM] -> 2 bits
     * @return
     */
    public static int placementLevelKey(Tenant tenant, PageType pt, Platform plt, ModuleLocation mLoc) {
        int mLocOrd = mLoc.ordinal();
        int pltOrd = plt.ordinal();
        int ptOrd = pt.ordinal();
        int tenantOrd = tenant.ordinal();
        return (tenantOrd << 7) | (ptOrd << 4) | (pltOrd << 2) | mLocOrd;
    }

    /**
     * Generate a key for enumeration. The purpose for this is replaced all enumeration string with one int, which can
     * save (N * 2 - 4) bytes assume N is the total number of enumeration's String representation.
     * Although combination here is just 8 bits, short seems better choice here, but fact is in Java, padding will be
     * used as single key, int is the reasonable choice here.
     *
     * @param Targeting [IN_MARKET/MAX_REACH] -> 2 bits
     * @param pt        [SEARCH/BROWSE/CATEGORY/ITEM/STOCKUP] -> 3 bites
     * @param plt       [MOBILE/APP/BROWSER] -> 2 bits
     * @param mLoc      [TOP/MIDDLE/BOTTOM] -> 2 bits
     * @return
     */
    public static int autoBidKey(Targeting Targeting, PageType pt, Platform plt, ModuleLocation mLoc) {
        int mLocOrd = mLoc.ordinal();
        int pltOrd = plt.ordinal();
        int ptOrd = pt.ordinal();
        int targetingOrd = Targeting.ordinal();
        return (targetingOrd << 7) | (ptOrd << 4) | (pltOrd << 2) | mLocOrd;
    }
}
