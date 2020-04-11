package com.walmart.labs.ads.keyword.model;

import com.walmart.labs.ads.keyword.datatype.CampaignType;

import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

public class AdItemCampaign {
    public static final AdItemCampaign FALLBACK_ADITEM_CAMPAIGN
            = new AdItemCampaign(0,0, 0, CampaignType.DEFAULT, 0, new HashMap<>(), 0d, "");

    @Id
    private final Long itemId;

    @Id
    private final int adGroupId;

    private final int campaignId;

    private final CampaignType campaignType;

    private final double cpc;

    private final Map<Integer, Double> cpcMap;

    private final double discount;

    private final String pangaeaSellerId;

    public AdItemCampaign(long itemId, int campaignId, int adGroupId, CampaignType campaignType, double cpc,
                          Map<Integer, Double> cpcMap, double discount, String pangaeaSellerId) {
        this.itemId = itemId;
        this.campaignId = campaignId;
        this.adGroupId = adGroupId;
        this.campaignType = campaignType;
        this.cpc = cpc;
        this.cpcMap = cpcMap;
        this.discount = discount;
        this.pangaeaSellerId = pangaeaSellerId;
    }

    @Override
    public int hashCode() {
        return itemId.hashCode() ^ adGroupId;
    }

    @Override
    public boolean equals(Object obj) {
        /**
         * Although it will be nice we should do null checking and class checking for equals operation, for efficiency
         * purpose here skip the checking.
         */
        AdItemCampaign other = (AdItemCampaign) obj;
        return this.itemId == other.itemId && this.adGroupId == other.adGroupId;
    }

    public long getItemId() {
        return itemId;
    }

    public int getAdGroupId() {
        return adGroupId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public CampaignType getCampaignType() {
        return campaignType;
    }

    public double getCpc() {
        return cpc;
    }

    public Map<Integer, Double> getCpcMap() {
        return cpcMap;
    }

    public double getDiscount() {
        return discount;
    }

    public String getPangaeaSellerId() {
        return pangaeaSellerId;
    }
}
