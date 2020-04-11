package com.walmart.labs.ads.keyword.util;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class UtilitiesTest {
    @DataProvider
    public static Object[][] taxomonyNormalizer() {
        return new Object[][]{
                {"5438_1045804_1045807_1234adid=22222222221202731355", "5438_1045804_1045807"},
                {"5438_1045804_1045807_1234", "5438_1045804_1045807"},
                {"5438_1045804_1045807adid=22222222221202731355", "5438_1045804_1045807"},
                {"5438_1045804_1045807", "5438_1045804_1045807"},
                {"5438_1045804adid=22222222221202731355", "5438_1045804"},
                {"5438_1045804", "5438_1045804"},
                {"5438adid=22222222221202731355", "5438"},
                {"5438", "5438"},
        };
    }

    @Test
    @UseDataProvider("taxomonyNormalizer")
    public void testTaxonomyNormalizer(String original, String expected) {
        assertEquals(expected, Utilities.normalize(original));
    }
    
    @DataProvider
    public static Object[][] categories() {
        return new Object[][]{
                {"/5438/1045804/1045807/12345/3325", "5438_1045804_1045807"},
                {"/5438/1045804/1045807/1234", "5438_1045804_1045807"},
                {"/5438/1045804/1045807", "5438_1045804_1045807"},
                {"/5438/1045804", "5438_1045804"},
                {"/5438", "5438"},
        };
    }

    @Test
    @UseDataProvider("categories")
    public void testParseCategory(String original, String expected) {
        assertEquals(expected, Utilities.parseRequestCategory(original));
    }

    @DataProvider
    public static Object[][] cpcMapProvider() {
        return new Object[][]{
                {"1:search:mobile:middle~0.34,2:browse:mobile:middle~0.34,0:search:mobile:middle~0.34," +
                        "0:browse:mobile:middle~0.3,2:search:desktop:middle~0.3,0:search:app:middle~0.3," +
                        "1:browse:mobile:middle~0.3", 7},
        };
    }

    @Test
    @UseDataProvider("cpcMapProvider")
    public void testParseCPCMap(String cpcMapString, int expected) {
        Map<Integer, Double> actual = Utilities.parseCPCMap(cpcMapString);
        assertEquals(expected, actual.size());
    }
}
