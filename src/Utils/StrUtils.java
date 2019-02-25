package Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {

    public static String igLinkSearchPattern = "(((instagram\\.com\\/)|(ig\\ ?\\-\\ ?))([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?))|(@([a-z0-9_]{1,255}))";
    public static String twitterLinkSearchPattern = "((https?:\\/\\/)?(www\\.)?twitter\\.com\\/)?(t\\.co\\/)?(@|#!\\/)?([a-zA-Z0-9_]{1,15})";
    public static String ipAddressPattern = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+$";

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

    public static boolean isProxyGrabbed(String response) {
        if (response.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(ipAddressPattern);
        Matcher matcher = pattern.matcher(response);
        return !matcher.find();
    }
}
