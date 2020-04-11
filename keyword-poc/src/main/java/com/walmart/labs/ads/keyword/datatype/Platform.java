package com.walmart.labs.ads.keyword.datatype;

public enum Platform {
    MOBILE,
    DESKTOP,
    APP;

    /**
     * Important for json deserialization. The name must be matched to HTTP request platform string otherwise
     * deserialization will be failed.
     * @see com.walmart.labs.ads.keyword.request.WMTBidRequest
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    };
}
