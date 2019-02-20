package Utils;

import org.apache.commons.lang3.StringUtils;

public class StrUtils {

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
}
