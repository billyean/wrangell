package com.walmart.labs.ads.keyword.model;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.walmart.labs.ads.keyword.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class MatcherTest {
    private Query mockQuery = Mockito.mock(Query.class);

    @DataProvider
    public static Object[][] exactTestProvider() {
        return new Object[][]{
                {new int[]{1, 2, 1}, new int[]{1, 2, 1}, true},
                {new int[]{1, 2,}, new int[]{2, 1}, false},
                {new int[]{1, 2}, new int[]{1, 2, 1}, false},
                {new int[]{2, 1}, new int[]{1, 2, 1}, false},
                {new int[]{1, 2, 1}, new int[]{1, 2}, false},
                {new int[]{1, 2, 1}, new int[]{2, 1}, false}
        };
    }

    @Test
    @UseDataProvider("exactTestProvider")
    public void testExactMatch(int[] data, int[] query, boolean expected) {
        when(mockQuery.getQuery()).thenReturn(query);
        ParsedKeywordBid.Matcher m = new ParsedKeywordBid.ExactMatcher(data);
        assertEquals(expected, m.match(mockQuery));
    }

    @DataProvider
    public static Object[][] phraseTestProvider() {
        return new Object[][]{
                {new int[]{1, 2, 1}, new int[]{1, 2, 1}, true},
                {new int[]{1, 1, 1}, new int[]{2, 1, 1, 1}, true},
                {new int[]{1, 1, 2}, new int[]{2, 1, 1, 2}, true},
                {new int[]{1, 1, 2}, new int[]{1, 1, 2, 3}, true},
                {new int[]{1, 2, 3}, new int[]{1, 2, 3, 4}, true},
                {new int[]{1, 2, 3}, new int[]{1, 2, 3, 1}, true},
                {new int[]{1, 2, 3}, new int[]{1, 1, 2, 3}, true},
                {new int[]{2, 3, 4}, new int[]{1, 2, 3, 4}, true},
                {new int[]{1, 2, 1}, new int[]{2, 1}, false},
                {new int[]{1, 2, 1}, new int[]{1, 2}, false},
                {new int[]{1, 2, 3}, new int[]{1, 2, 4, 3}, false},
                {new int[]{1, 2, 3}, new int[]{1, 2, 2, 3}, false},
                {new int[]{1, 1, 2}, new int[]{1, 2, 1, 3}, false},
        };
    }

    @Test
    @UseDataProvider("phraseTestProvider")
    public void testPhraseMatch(int[] data, int[] query, boolean expected) {
        when(mockQuery.getQuery()).thenReturn(query);
        ParsedKeywordBid.Matcher m = new ParsedKeywordBid.PhraseMatcher(data);
        assertEquals(expected, m.match(mockQuery));
    }

    @DataProvider
    public static Object[][] broadTestProvider() {
        return new Object[][]{
                {new int[]{1, 2, 1}, new int[]{1, 2, 1}, true},
                {new int[]{1, 2,}, new int[]{2, 1}, true},
                {new int[]{1, 2,}, new int[]{1, 2, 1}, true},
                {new int[]{1, 2,}, new int[]{1, 2, 2}, true},
                {new int[]{1, 3, 2}, new int[]{1, 2, 3}, true},
                {new int[]{1, 2, 3}, new int[]{1, 2, 3, 4}, true},
                {new int[]{1, 3, 2}, new int[]{1, 2, 3, 4}, true},
                {new int[]{2, 1, 3}, new int[]{1, 2, 3, 4}, true},
                {new int[]{2, 3, 1}, new int[]{1, 2, 3, 4}, true},
                {new int[]{2, 3, 1}, new int[]{1, 2, 3, 4}, true},
                {new int[]{3, 1, 2}, new int[]{1, 2, 3, 4}, true},
                {new int[]{3, 2, 1}, new int[]{1, 2, 3, 4}, true},
                {new int[]{1, 2, 3}, new int[]{1, 2, 2, 3}, true},
                {new int[]{1, 3, 2}, new int[]{1, 3, 3, 2}, true},
                {new int[]{2, 1, 3}, new int[]{2, 2, 1, 3}, true},
                {new int[]{2, 3, 1}, new int[]{2, 1, 3, 3}, true},
                {new int[]{2, 3, 1}, new int[]{2, 1, 3, 2}, true},
                {new int[]{3, 1, 2}, new int[]{2, 2, 3, 1}, true},
                {new int[]{3, 2, 1}, new int[]{3, 2, 3, 1}, true},
                {new int[]{3}, new int[]{1, 1, 1}, false},
                {new int[]{2, 1}, new int[]{1, 1, 1}, false},
                {new int[]{2, 1, 1}, new int[]{1, 2, 2}, false},
                {new int[]{1, 1, 1}, new int[]{1, 1, 2}, false},
                {new int[]{1, 1, 1}, new int[]{1, 1, 2, 2}, false},
                {new int[]{1, 2, 1}, new int[]{1, 2, 3}, false},
                {new int[]{1, 2, 2, 2, 3}, new int[]{1, 1, 2, 2, 3}, false}
        };
    }

    @Test
    @UseDataProvider("broadTestProvider")
    public void testBroadMatch(int[] data, int[] query, boolean expected) {
        when(mockQuery.getQuery()).thenReturn(query);
        ParsedKeywordBid.Matcher m = new ParsedKeywordBid.BroadMatcher(data);
        assertEquals(expected, m.match(mockQuery));
    }
}
