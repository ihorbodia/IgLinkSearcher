package Utils;

import Models.CsvItemModel;
import Models.SearchResult;
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

                if (mainHeaderResult.contains(csvItem.getURLToLower())
                        || descriptionResult.contains(csvItem.getURLToLower())
                        || searchedLinkResult.contains(csvItem.getURLToLower())) {
                    res = result;
                } else if (mainHeaderResult.trim().contains(csvItem.getURLToLower().trim())
                        || descriptionResult.trim().contains(csvItem.getURLToLower().trim())
                        || searchedLinkResult.trim().contains(csvItem.getURLToLower().trim())) {
                    res = result;
                } else if (mainHeaderResult.contains(csvItem.getCompanyNameToLower()) ||
                        descriptionResult.contains(csvItem.getCompanyNameToLower()) ||
                        searchedLinkResult.contains(csvItem.getCompanyNameToLower())) {
                    res = result;
                } else if (mainHeaderResult.trim().contains(csvItem.getCompanyNameToLower().trim()) ||
                        descriptionResult.trim().contains(csvItem.getCompanyNameToLower().trim()) ||
                        searchedLinkResult.trim().contains(csvItem.getCompanyNameToLower().trim())) {
                    res = result;
                } else if (mainHeaderResult.contains(csvItem.getPureNameToLower()) ||
                        descriptionResult.contains(csvItem.getPureNameToLower()) ||
                        searchedLinkResult.contains(csvItem.getPureNameToLower())) {
                    res = result;
                }
            }
        }
        return res;
    }

    public static String getSocialAccountFromString(String value, String regexPattern){
        String result = null;
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            if (matcher.group(5) != null && matcher.group(5).length() > 4) {
                result = StrUtils.normalizeLink(matcher.group(0));
            }
        }
        return result;
    }
}