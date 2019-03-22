package Utils;

import Models.CsvItemModel;
import org.tinylog.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtils {

    public static String createURLForTwitterSearch(CsvItemModel item) {
        String searchTerm = "twitter " + StrUtils.extractWebSiteFromLongLink(item.URL);
        String result = null;
        try {
            result = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr&num=15";
        } catch (UnsupportedEncodingException e) {
            Logger.error(e);
        }
        return result;
    }

    public static String createURLForIgSearch(CsvItemModel item) {
        String searchTerm = "site:www.instagram.com "+ StrUtils.extractWebSiteFromLongLink(item.URL);
        String result = null;
        try {
            result = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr&num=15";
        } catch (UnsupportedEncodingException e) {
            Logger.error(e);
        }
        return result;
    }

//    public static String clearLink(String link) {
//        if (StringUtils.isEmpty(link)) {
//            return "";
//        }
//        link = link.replace("http://www.google.com/url?url=", "");
//        if (link.startsWith("www")) {
//            link = "http://" + link;
//        }
//        if (link.startsWith("/url")) {
//            link = link.substring(link.indexOf("=") + 1);
//        }
//        if (link.indexOf("&") > 0) {
//            link = link.substring(0, link.indexOf("&"));
//        }
//        return link;
//    }
}
