package Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {

    public static String igLinkSearchPattern = "((https?:\\/\\/)?(www\\.)?((instagram\\.com\\/)|(ig\\ ?\\-\\ ?))(?!\\/p\\/)(?!explore)(?!hashtag)[A-Za-z0-9_]{4,20})|(@([a-z0-9_]{4,255}))";
    public static String twitterLinkSearchPattern = "((https?:\\/\\/)?(www\\.)?(twitter\\.com\\/)|(t\\.co\\/))(?!login)(?!hashtag)([a-zA-Z0-9_]{4,20})|(@([a-z0-9_]{4,255}))";
    public static String webSiteRegex = "((https?://)?(www\\.)://)?(([\\w.-]*)\\.([\\w]*))";
    public static String pureWebSiteRegex = "([a-zA-Z0-9_]{4,255}\\.[a-zA-Z0-9]{1,5})";
    //public static String ipAddressPattern = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+$";

    public static String getLinkFromURL(String link, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return link;
    }

    public static String getInstagramLinkFromURL(String link) {
        Pattern pattern = Pattern.compile(igLinkSearchPattern);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return link;
    }

    public static String getTwitterLinkFromURL(String link) {
        Pattern pattern = Pattern.compile(twitterLinkSearchPattern);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return link;
    }

    public static String getPureLinkFromURL(String link) {
        Pattern pattern = Pattern.compile(pureWebSiteRegex);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return link;
    }

    public static String cutPath(String path) {
        int size = 80;
        if (path.length() <= size) {
            return path;
        } else {
            return "..." + path.substring(path.length() - (size - 3));
        }
    }

    public static String extractWebSiteFromLongLink(String link) {
        Pattern pattern = Pattern.compile(webSiteRegex);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return link;
    }
}
