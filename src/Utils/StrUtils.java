package Utils;

import org.apache.commons.lang3.StringUtils;

public class StrUtils {

    public static String igLinkSearchPattern = "(((instagram\\.com\\/)|(ig\\ ?\\-\\ ?))([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?))|(@([a-z0-9_]{1,255}))";
    public static String twitterLinkSearchPattern = "((https?:\\/\\/)?(www\\.)?twitter\\.com\\/)?(t\\.co\\/)?(@|#!\\/)?([A-Za-z0-9_]{1,15})(\\/([-a-z]{1,20}))?";

    public static String normalizeLink(String link) {
        if (StringUtils.isEmpty(link)) {
            return "";
        }
        String result;
        if (link.startsWith("http://") || link.startsWith("https://")) {
            result = link;
        } else if (link.startsWith("www")) {
            result = "http://" + link;
        } else {
            result = "http://www." + link;
        }
        return result;
    }


    public static String cutPath(String path) {
        int size = 60;
        if (path.length() <= size) {
            return path;
        } else {
            return "..." + path.substring(path.length() - (size - 3));
        }
    }
}
