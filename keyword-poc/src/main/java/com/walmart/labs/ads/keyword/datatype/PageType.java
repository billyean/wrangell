package com.walmart.labs.ads.keyword.datatype;

public enum PageType {
    SEARCH,
    BROWSE,
    CATEGORY,
    STOCKUP,
    ITEM;

    /**
     * Important for json deserialization. The name must be matched to HTTP request pageType string otherwise
     * deserialization will be failed.
     * @see com.walmart.labs.ads.keyword.request.WMTBidRequest
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    };
}
