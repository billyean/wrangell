package com.walmart.labs.ads.keyword.serialization;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class KeywordBidKeySerializerTest {
    @DataProvider
    public static Object[][] serializationDataProvider() {
        return new Object[][] {
                {-1L << 33},
                {-1L << 16},
                {-10000L},
                {-100L},
                {-1L},
                {0L},
                {1L},
                {100L},
                {10000L},
                {1 << 16L},
                {1L << 33L}
        };
    }

    @Test
    @UseDataProvider("serializationDataProvider")
    public void testSerialization(long testInt) {
        KeywordBidKeySerializer serializer = new KeywordBidKeySerializer();
        byte[] serialized = serializer.serialize(testInt);

        long deserialized = serializer.deserialize(serialized);
        assertEquals(testInt, deserialized);
    }
}
