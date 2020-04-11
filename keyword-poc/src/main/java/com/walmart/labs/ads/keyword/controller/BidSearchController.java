package com.walmart.labs.ads.keyword.controller;

import com.walmart.labs.ads.keyword.db.mysql.MySQLBidService;
import com.walmart.labs.ads.keyword.model.MatchedBid;
import com.walmart.labs.ads.keyword.request.WMTBidRequest;
import com.walmart.labs.ads.keyword.service.SearchBestBidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * This is a wrapper for rest API. No real logic here.
 */
@RestController
public class BidSearchController {
    private static final Logger log = LoggerFactory.getLogger(BidSearchController.class);

    @Autowired
    SearchBestBidService searchService;

    @PostMapping(value = "/v1/wmt/search/bids", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<MatchedBid> bid(@Valid @RequestBody WMTBidRequest request) {
        log.info("{}", request.getItems());
        return searchService.search(request);
    }
}
