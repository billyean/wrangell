package com.walmart.labs.ads.keyword.datatype;

/**
 * When two match has same CPC, exact match trumps phrase match and phrase match trump broad match.
 */
public enum MatchType {
    BROAD,
    PHRASE,
    EXACT;

    /**
     * Important for json deserialization. The name must be matched to database matchType string otherwise
     * deserialization will be failed.
     * @return
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    };
}
