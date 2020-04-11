package com.walmart.labs.ads.keyword.datatype;

/**
 * Enum for Walmart's application type. Currently only two type supported.
 * GR:  Grocery
 * WMT: Online
 */
public enum Tenant {
    GR,
    WMT;

    /**
     * Important for jackson deserialization. The name must be matched to database tenant string otherwise
     * deserialization will be failed.
     * @return lower case string represent for online grocery(GR) or online shopping(WMT).
     * @see com.walmart.labs.ads.keyword.request.WMTBidRequest
     */
    @Override
    public String toString() {
        return this.name();
    };
}
