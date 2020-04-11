package com.walmart.labs.ads.keyword.model;

import com.walmart.labs.ads.keyword.datatype.MatchType;

public class MatchedBid {
    public long getItemId() {
        return itemId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public int getAdGroupId() {
        return adGroupId;
    }

    public String getKeyword() {
        return keyword;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public String getPgProdId() {
        return pgProdId;
    }

    public void setPgProdId(String pgProdId) {
        this.pgProdId = pgProdId;
    }

    public String getRefId() {
        return refId;
    }

    public double getCpc() {
        return cpc;
    }

    public String getWpa_bd() {
        return wpa_bd;
    }

    public String getPgSellerId() {
        return pgSellerId;
    }

    private final long itemId;

    private final int  campaignId;

    private final int  adGroupId;

    private final String keyword;

    private final MatchType matchType;

    private String pgProdId;

    private final String refId;

    private final double cpc;

    private final String wpa_bd;

    private final String pgSellerId;

    public MatchedBid(long itemId, int campaignId, int adGroupId, String keyword,
                      MatchType matchType, String refId, double cpc, String pgSellerId) {
        this.itemId = itemId;
        this.campaignId = campaignId;
        this.adGroupId = adGroupId;
        this.keyword = keyword;
        this.matchType = matchType;
        this.refId = refId;
        this.cpc = cpc;
        this.wpa_bd = String.valueOf(System.currentTimeMillis());
        this.pgSellerId = pgSellerId;
    }
}
