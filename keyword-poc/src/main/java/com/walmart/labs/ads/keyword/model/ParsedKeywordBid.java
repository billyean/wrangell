package com.walmart.labs.ads.keyword.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.labs.ads.keyword.Query;
import com.walmart.labs.ads.keyword.datatype.MatchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class ParsedKeywordBid {
    private static final Logger log = LoggerFactory.getLogger(ParsedKeywordBid.class);

    /**
     * This class only for json deserialization.
     */
    final static class KeywordBidContent {
        List<String> keywords;

        double cpc;

        MatchType matchType;

        public KeywordBidContent() {
            this.keywords = Collections.emptyList();
            this.matchType = MatchType.EXACT;
            this.cpc = 0d;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public MatchType getMatchType() {
            return matchType;
        }

        public double getCpc() {
            return cpc;
        }

        public KeywordBidContent(List<String> keywords, MatchType matchType, double cpc) {
            this.keywords = keywords;
            this.matchType = matchType;
            this.cpc = cpc;
        }

        Stream<ParsedKeywordBid> toKeywordBids() {
            return keywords.stream().map(k -> new ParsedKeywordBid(k, cpc, matchType));
        }
    }
    /**
     * A fixed pattern string for keyword content. The format of keyword content is a keyword content id following a
     * sequence of keyword separated by space.
     * Example:
     *    "2122981~neocuti lumiere bio restore eye crea"
     *    "2122978~neocuti"
     *    "2122562~plastic 2 pocket folder"
     *
     */
    private final static Pattern KEYWORD_CONTENT_PATTERN = Pattern.compile("[0-9]+~(.*)");

    private final static KeywordIndex lookupServ = KeywordIndex.instance();

    /**
     * A ParsedKeywordBid constructor from string.
     * @param keywords
     * @param cpc
     * @param matchType
     */
    public ParsedKeywordBid(String keywords, double cpc, MatchType matchType) {
        this.cpc = cpc;
        this.matchType = matchType;

        /**
         * Build all position indices by all keywords. Here also make sure every keywords are in-place in global Keyword
         * indices map.
         * Any error here will not be thrown instead a corrupted ParsedKeywordBid instance will reserved in the memory.
         * This won't stop application but this will stop current ParsedKeywordBid to be used in further steps.
         */
        try {
            List<Integer> indices = new ArrayList<>();
            java.util.regex.Matcher m = KEYWORD_CONTENT_PATTERN.matcher(keywords);
            if (m.matches()) {
                this.keywords = m.group(1);
                java.util.regex.Matcher mk = KEYWORD_PATTERN.matcher(m.group(1));
                while (mk.find()) {
                    String capturing = mk.group();
                    int index = lookupServ.addAndFecthIndex(capturing);
                    indices.add(index);
                }
            }
            keywordIndices = indices.stream().mapToInt(Integer::intValue).toArray();
        } catch (Exception ex) {
            keywordIndices = new int[0];
        }

        switch(matchType) {
            case EXACT:
                matcher = new ExactMatcher(this.keywordIndices);
                break;
            case PHRASE:
                matcher = new PhraseMatcher(this.keywordIndices);
                break;
            default:
                matcher = new BroadMatcher(this.keywordIndices);
                break;
        }
    }

    /**
     * A ParsedKeywordBid constructor from serialized indices data.
     * @param indices
     * @param cpc
     * @param matchType
     */
    public ParsedKeywordBid(int[] indices, double cpc, MatchType matchType) {
        this.cpc = cpc;
        this.matchType = matchType;
        this.keywordIndices = indices;

        this.keywords = Arrays.stream(indices).mapToObj(lookupServ::keywordAtIndex).collect(joining(" "));

        switch(matchType) {
            case EXACT:
                matcher = new ExactMatcher(this.keywordIndices);
                break;
            case PHRASE:
                matcher = new PhraseMatcher(this.keywordIndices);
                break;
            default:
                matcher = new BroadMatcher(this.keywordIndices);
                break;
        }
    }

    /**
     * validation that if current ParsedKeywordBid match given query.
     * @param query
     * @return true if matched, false otherwise.
     */
    public boolean match(Query query) {
        log.info(this.toString());
        return matcher.match(query);
    }

    /**
     * Validation of ParsedKeywordBid.
     * @return
     */
    public boolean valid() {
        return keywordIndices.length != 0;
    }

    /**
     * Match all alphabetic-number.
     */
    private final static Pattern KEYWORD_PATTERN = Pattern.compile("[^\\s]+");

    private final static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

    private String keywords;

    private final double cpc;

    private final MatchType matchType;

    private final Matcher matcher;

    @JsonIgnore
    private int[] keywordIndices;

    /**
     * Deserialize given json string into a ParsedKeywordBid list.
     * @param keywordsJsonString
     * @return a list of ParsedKeywordBid instance deserialized by given json string.
     * @throws JsonProcessingException
     */
    public static Set<ParsedKeywordBid> parseJson(String keywordsJsonString) throws JsonProcessingException {
        List<KeywordBidContent> cpcKeyWords =  mapper.readValue(keywordsJsonString,
                mapper.getTypeFactory().constructCollectionType(List.class, KeywordBidContent.class));
//
//        for (KeywordBidContent content: cpcKeyWords) {
//            System.out.println("\t\t\t\tnew ArrayList<>(){{");
//            content.keywords.forEach(v -> System.out.println("\t\t\t\t\tadd(\"" + v + "\");"));
//            System.out.print("\t\t\t\t}}, MatchType." + content.matchType.toString().toUpperCase() + ", ");
//            System.out.println(content.cpc);
//
//        }
        return cpcKeyWords.stream().flatMap(KeywordBidContent::toKeywordBids).collect(Collectors.toSet());
    }

    public int[] getKeywordIndices() {
        return keywordIndices;
    }

    public String getKeywords() {
        return keywords;
    }

    public double getCpc() {
        return cpc;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    @Override
    public int hashCode() {
        int hash = keywords.hashCode();
        hash ^= matchType.ordinal() * 199933 + 199933;
        hash ^= Double.hashCode(cpc);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !other.getClass().equals(this.getClass())) {
            return false;
        }

        ParsedKeywordBid checked = (ParsedKeywordBid)other;
        return checked.cpc == cpc && checked.matchType == matchType && checked.keywords.equals(keywords);
    }

    @Override
    public String toString() {
        return String.format("Keywords: %s, MatchType: %s, cpc: %8.3f", keywords, matchType, cpc);
    }

    static abstract class Matcher {
        protected final int[] indices;

        public Matcher(int[] indices) {
            this.indices = indices;
        }

        public abstract boolean match(Query query);
    }

    /**
     * A array based exact match query.
     */
    static class ExactMatcher extends Matcher {
        public ExactMatcher(int[] indices) {
            super(indices);
        }

        @Override
        public boolean match(Query query) {
            int[] lookup = query.getQuery();
            if (lookup.length != indices.length)
                return false;
            for (int i = 0; i < lookup.length; i++) {
                if (lookup[i] !=(indices[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * A Knuth–Morris–Pratt search algorithm for search sub array search. This actually won't help too much on our sub
     * array search because
     * 1. Keyword don't have too much duplicate word, which makes most of times prefix is an array with zero value.
     * 2. Length of the array is short, most of time less than 10.
     */
    static class PhraseMatcher extends Matcher {
        private final int[] prefix;

        public PhraseMatcher(int[] indices) {
            super(indices);
            prefix = new int[indices.length];

            for (int k = 0, j = 2; j <=indices.length; j++) {
                while (k > 0 && indices[k - 1] != indices[j - 1]) {
                    k = prefix[k - 1];
                }
                if (indices[k] == indices[j - 1]) {
                    k++;
                }
                prefix[j - 1] = k;
            }
        }

        @Override
        public boolean match(Query query) {
            int[] lookup = query.getQuery();
            int i = 0;
            int j = 0;

            while (i < lookup.length && j < prefix.length) {
                if (lookup[i] == indices[j]) {
                    i++;
                    j++;
                } else if (j > 0) {
                    j = prefix[j - 1];
                } else {
                    i++;
                }
            }

            return j == prefix.length;
        }
    }

    static class BroadMatcher extends Matcher {
        private final HashMap<Integer, Integer> kpMap;

        public BroadMatcher(int[] indices) {
            super(indices);
            kpMap = new HashMap<>();
            for (int v : this.indices) {
                kpMap.put(v, kpMap.getOrDefault(v, 0) + 1);
            }
        }

        @Override
        public boolean match(Query query) {
            HashMap<Integer, Integer> lookupMap = new HashMap<>();
            boolean atLeastOne = false;
            for (int v : query.getQuery()) {
                lookupMap.put(v, lookupMap.getOrDefault(v, 0) + 1);
            }

            for (Map.Entry<Integer, Integer> entry: kpMap.entrySet()) {
                if (lookupMap.containsKey(entry.getKey())) {
                    atLeastOne = true;
                    if (entry.getValue() > lookupMap.get(entry.getKey())) {
                        return false;
                    }
                }
            }
            return atLeastOne;
        }
    }
}
