package Utils;

import Models.CsvItemModel;
import Models.SearchResultItem;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchUtils {

    public static boolean isHasInstagramSearchResultCriteria(String value) {
        return (StringUtils.isEmpty(value) || value.length() > 10) &&
                (value.toLowerCase().contains("instagram.") || value.toLowerCase().contains("ig.")) &&
                !value.contains("instagram.com/explore/");
    }

    public static boolean isHasTwitterSearchResultCriteria(String value) {
        return (StringUtils.isEmpty(value) || value.length() > 10) &&
                (value.toLowerCase().contains("twitter.") || value.toLowerCase().contains("t.co")) &&
                !value.contains("twitter.com/hashtag/");
    }




}
