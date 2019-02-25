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

    public static SearchResultItem getRightResultItem(List<SearchResultItem> items, CsvItemModel csvItem) {
        SearchResultItem res = null;
        if (items.size() > 0) {
            for (SearchResultItem result : items) {
                String mainHeaderResult = result.MainHeader.toLowerCase();
                String descriptionResult = result.Description.toLowerCase();
                String searchedLinkResult = result.SearchedLink.toLowerCase();

                if (StringUtils.containsAny(mainHeaderResult, csvItem.getURLToLower(), csvItem.getCompanyNameToLower(), csvItem.getPureNameToLower()) ||
                    StringUtils.containsAny(descriptionResult, csvItem.getURLToLower(), csvItem.getCompanyNameToLower(), csvItem.getPureNameToLower()) ||
                    StringUtils.containsAny(searchedLinkResult, csvItem.getURLToLower(), csvItem.getCompanyNameToLower(), csvItem.getPureNameToLower()) ||

                    StringUtils.containsAny(mainHeaderResult.trim(), csvItem.getURLToLower().trim(), csvItem.getCompanyNameToLower().trim()) ||
                    StringUtils.containsAny(descriptionResult.trim(), csvItem.getURLToLower().trim(), csvItem.getCompanyNameToLower().trim()) ||
                    StringUtils.containsAny(searchedLinkResult.trim(), csvItem.getURLToLower().trim(), csvItem.getCompanyNameToLower().trim()))
                {
                    res = result;
                    break;
                }
            }
        }
        return res;
    }

    public static String getSocialAccountFromString(String value, String regexPattern) {
        String result = null;
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            result = StrUtils.normalizeLink(matcher.group(0));
        }
        return result;
    }
}
