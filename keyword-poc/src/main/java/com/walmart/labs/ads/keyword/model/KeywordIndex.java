package com.walmart.labs.ads.keyword.model;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A global map map single keyword with its index. The index of keyword is static in the whole lifecycle of application.
 * All unmatched(Not existed in database) keyword will have same index MISSING_POS, this makes match search more
 * efficient.
 * KeywordIndex is sharing between tenant.
 *
 */
public class KeywordIndex {
    /**
     * Singleton instance.
     */
    private static final KeywordIndex internal = new KeywordIndex();

    /**
     * Global singleton keyword -> index map.
     * This thread-safe map can prevent copying all value when need to update.(Especially when do any full refresh
     * operation).
     */
    private final ConcurrentHashMap<String, Integer> keywordIndexMap;

    /**
     * Global singleton index -> keyword map.
     * This thread-safe map can prevent copying all value when need to update.(Especially when do any full refresh
     * operation).
     */
    private final ConcurrentHashMap<Integer, String> indexKeywordMap;

    /**
     * A global generator for the index.
     */
    private final AtomicInteger currentIndex;

    /**
     * Default index for not matching keyword. When parsing a query string
     */
    private static final int MISSING_KEYWORD_INDEX = 0;

    /**
     * Default keyword for not matching index.
     */
    private static final String MISSING_INDEX_KEYWORD = "";

    public KeywordIndex() {
        keywordIndexMap = new ConcurrentHashMap<>();
        indexKeywordMap = new ConcurrentHashMap<>();
        /**
         * Keep index start from one instead of zero. While zero is for missing keyword.
         */
        currentIndex = new AtomicInteger(1);
    }

    public static KeywordIndex instance() {
        return internal;
    }

    public int size() {
        return keywordIndexMap.size();
    }

    /**
     * Insert new keyword and index into global map, otherwise do nothing.
     */
    public int addAndFecthIndex(String word) {
        int index = keywordIndexMap.getOrDefault(word, currentIndex.getAndIncrement());
        keywordIndexMap.put(word, index);
        indexKeywordMap.put(index, word);
        return index;
    }

    /**
     * Lookup the index by given string, zero will be returned if string is missing.
     * @param lookup
     * @return
     */
    public Integer currentPos(String lookup) {
        return keywordIndexMap.getOrDefault(lookup, MISSING_KEYWORD_INDEX);
    }

    /**
     * Lookup the keyword by given index, zero will be returned if string is missing.
     * @param index
     * @return
     */
    public String keywordAtIndex(int index) {
        return indexKeywordMap.getOrDefault(index, MISSING_INDEX_KEYWORD);
    }

    /**
     * Convert a string array to a index array.
     * @param sequences
     * @return
     */
    public final int[] concise(String[] sequences) {
        return Arrays.stream(sequences).map(this::currentPos).mapToInt(Integer::intValue).toArray();
    }
}
