package com.walmart.labs.ads.keyword.util;

import com.walmart.labs.ads.keyword.datatype.ModuleLocation;
import com.walmart.labs.ads.keyword.datatype.PageType;
import com.walmart.labs.ads.keyword.datatype.Platform;
import com.walmart.labs.ads.keyword.datatype.Targeting;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    /**
     * A regular expression pattern to capture first 3 part of taxonomy.
     *
     * +-----------------------------------------------+
     * | taxonomy                                      |
     * +-----------------------------------------------+
     * | 5427_8825382adid=22222222221                  |
     * | 5428_5026924adid=22222222220430463324         |
     * | 5428_5026924adid=22222222221430463323         |
     * | 5428_5026924adid=22222222221430463338         |
     * | 5438_1045804_1045807adid=22222222221202731355 |
     * +-----------------------------------------------+
     */
    private static final Pattern TAXONOMY_CAPTURE_PATTEN =  Pattern.compile("(\\d+(_\\d+){0,2})(_\\d+)*(adid=\\d+)*");

    /**
     * A regular expression version to normalize taxonomy.
     * @param taxonomy
     * @return
     */
    public static String normalize(String taxonomy) {
        Matcher m = TAXONOMY_CAPTURE_PATTEN.matcher(taxonomy);
        if (m.matches() && m.groupCount() > 0) {
            return m.group(1);
        } else {
            return taxonomy;
        }
    }

    /**
     * A regular expression pattern to capture first 3 part of category and form a string.
     *
     * +-----------------------------------------------+
     * | category                                      |
     * +-----------------------------------------------+
     * | /5427                                         |
     * | /5427/8825382                                 |
     * | /5438/1045804/1045807                         |
     * | /5438/1045804/1045807/12345                   |
     * | /5438/1045804/1045807/12345/3325              |
     * +-----------------------------------------------+
     */
    private static final Pattern CATEGORY_CAPTURE_PATTEN =  Pattern.compile("(/(\\d+))(/(\\d+))?(/(\\d+))?(/\\d+)*");

    /**
     * A regular expression version to normalize taxonomy.
     * @param category
     * @return
     */
    public static String parseRequestCategory(String category) {
        Matcher m = CATEGORY_CAPTURE_PATTEN.matcher(category);
        if (m.matches() && m.groupCount() > 0) {
            StringJoiner joiner = new StringJoiner("_");
            joiner.add(m.group(2));
            if (m.group(4) != null) {
                joiner.add(m.group(4));
            }
            if (m.group(6) != null) {
                joiner.add(m.group(6));
            }
            return joiner.toString();
        } else {
            return category;
        }
    }

    /**
     * 0:search:app:middle~0.3
     * 0:search:mobile:middle~0.34
     * 0:browse:mobile:middle~0.3
     * 1:search:mobile:middle~0.34
     * 2:browse:mobile:middle~0.34
     * 2:search:desktop:middle~0.3
     * 1:browse:mobile:middle~0.3
     */
    private static final Pattern AUTO_BID_CPC_PATTEN =
            Pattern.compile("([0-2]):(search|browse|category|item|stockup):(mobile|desktop|app):(top|middle|bottom)~(\\d+([.]\\d+)?)");

    /**
     * This will parse cpc map string into a real HashMap, key is the auto bid cpc key, value is cpc value.
     * Auto bid cpc key is an integer value represent Targeting + PageType + Platform + Module_Location
     * @param cpcMapString
     * @return
     */
    public static Map<Integer, Double> parseCPCMap(String cpcMapString) {
        if(cpcMapString == null || cpcMapString.isEmpty()) return Collections.EMPTY_MAP;

        String[] cpcStrings = cpcMapString.split(",");
        Map<Integer, Double> cpcMap = new HashMap<>();

        for (String cpcString: cpcStrings) {
            Matcher m = AUTO_BID_CPC_PATTEN.matcher(cpcString);
            if (m.matches()) {
                Targeting targeting = Targeting.withCustomValue(m.group(1));
                PageType pt = PageType.valueOf(m.group(2).toUpperCase());
                Platform plt = Platform.valueOf(m.group(3).toUpperCase());
                ModuleLocation mLoc = ModuleLocation.valueOf(m.group(4).toUpperCase());
                double cpc = Double.parseDouble(m.group(5));
                cpcMap.put(KeyGenerator.autoBidKey(targeting, pt, plt, mLoc), cpc);
            }
        }

        return cpcMap;
    }
}
