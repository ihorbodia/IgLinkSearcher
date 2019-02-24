package Utils;

import Models.CsvItemModel;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtils {

    public static String createURLForTwitterSearch(CsvItemModel item) {
        String searchTerm = "twitter " + item.getPureName();
        String result = null;
        try {
            result = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr&num=25";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String createURLForIgSearch(CsvItemModel item) {
        String searchTerm = "site:www.instagram.com " + item.companyName + " " + item.getPureName() + " " + item.URL;
        String result = null;
        try {
            result = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr&num=25";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String clearLink(String link) {
        if (StringUtils.isEmpty(link)) {
            return "";
        }
        link = link.replace("http://www.google.com/url?url=", "");
        if (link.startsWith("www")) {
            link = "http://" + link;
        }
        if (link.startsWith("/url")) {
            link = link.substring(link.indexOf("=") + 1);
        }
        if (link.indexOf("&") > 0) {
            link = link.substring(0, link.indexOf("&"));
        }
        return link;
    }
}
