package com.walmart.labs.ads.keyword;

import com.walmart.labs.ads.keyword.model.KeywordIndex;

import java.util.Arrays;

public class Query {
    protected final static KeywordIndex lookupSrv = KeywordIndex.instance();

    private final String original;

    private final int[] query;

    private final int[] sortedQuery;

    public Query(String queryString) {
        this.original = queryString;
        String[] splitQuery = queryString.split(" ");
        this.query = lookupSrv.concise(splitQuery);
        this.sortedQuery = new int[this.query.length];
        System.arraycopy(this.query, 0, sortedQuery, 0, splitQuery.length);
        Arrays.sort(sortedQuery);
    }

    public String getOriginal() {
        return original;
    }

    public int[] getQuery() {
        return query;
    }

    public int[] getSortedQuery() {
        return sortedQuery;
    }
}
