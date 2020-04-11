package com.walmart.labs.ads.keyword.datatype;

public enum SiteMode {
    SITE_MODE_DEFAULT("0"),
    SITE_MODE_ND("1");

    private String sMode;

    SiteMode(String sMode) {
        this.sMode = sMode;
    }

    /**
     * Important for json deserialization. The name must be matched to HTTP request siteMode string otherwise
     * deserialization will be failed.
     * @see com.walmart.labs.ads.keyword.request.WMTBidRequest
     */
    @Override
    public String toString() {
        return this.sMode;
    };
}
