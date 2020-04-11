package com.walmart.labs.ads.keyword.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Bid Request for Walmart online shopping.
 */
public class WMTBidRequest extends BasedBidRequest {
    private static final Logger log = LoggerFactory.getLogger(WMTBidRequest.class);

    public Stream<Long> getItemsStream() {
        return Arrays.stream(items.split(",")).map(Long::parseLong);
    }

    public String getItems() {
        return this.items;
    }

    /**
     * Item list for looking up best bid.
     */
    @NotEmpty(message = "Please provide a list of item ID.")
    private String items;
}
