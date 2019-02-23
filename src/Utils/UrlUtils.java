package Utils;

import Models.CsvItemModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtils {

    public static String createURLForTwitterSearch(CsvItemModel item) {
        String searchTerm = "twitter " + item.getPureName();
        String result = null;
        try {
            result = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String createURLForIgSearch(CsvItemModel item) {
        String searchTerm = "site:www.instagram.com " + item.companyName + " " + item.getPureName() + " " + item.URL;
        String result = null;
        try {
            result = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
