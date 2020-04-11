package com.walmart.labs.ads.keyword.cache;

import com.walmart.labs.ads.keyword.Query;
import com.walmart.labs.ads.keyword.datatype.Tenant;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;

@Service
public interface KeywordLookupService {
    void addAdGroupKeywordBid(Tenant tenant, int adGroupId, Set<ParsedKeywordBid> keywordBis);

    Map<Integer, ParsedKeywordBid> queryMatching(Tenant tenant, String searchQuery, Set<Integer> adGroupIds);

    Set<ParsedKeywordBid> search(Tenant tenant, int adGroup);

    /**
     * Find best bid using query. The definition of best bid is the highest cpc in all parsedKeywordBids matched the
     * given query.
     * @param query
     * @param parsedKeywordBids
     * @return
     */
    default Optional<ParsedKeywordBid> bestBid(Query query, Set<ParsedKeywordBid> parsedKeywordBids) {
        if (parsedKeywordBids == null)  return empty();
        return parsedKeywordBids.stream().filter(bid -> bid.match(query))
                .max(Comparator.comparing(ParsedKeywordBid::getCpc));
    }
}
