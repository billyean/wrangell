package com.walmart.labs.ads.keyword.datatype;

public enum Targeting {
    IN_MARKET("0"),
    UNCLEAR("1"),
    MAX_REACH("2");

    private String targeting;

    Targeting(String targeting) {
        this.targeting = targeting;
    }

    /**
     * Important for json deserialization. The name must be matched to HTTP request targeting string otherwise
     * deserialization will be failed.
     * @see com.walmart.labs.ads.keyword.request.WMTBidRequest
     */
    @Override
    public String toString() {
        return this.targeting;
    };

    public static Targeting withCustomValue(String cv) {
        switch(cv) {
            case "0":   return IN_MARKET;
            case "2":   return MAX_REACH;
            default:    return UNCLEAR;
        }
    }
}
