package Utils;

import Models.CsvItemModel;
import org.tinylog.Logger;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtils {

    public static String createURLForTwitterSearch(CsvItemModel item) {
        String searchTerm = item.getBaseURL() + " inurl:twitter.com";
        String result = null;
        try {
            result = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr&num=15";
        } catch (UnsupportedEncodingException e) {
            Logger.error(e);
        }
        return result;
    }

    public static String createURLForIgSearch(CsvItemModel item) {
        String searchTerm = String.format("(%s | %s) inurl:instagram.com", item.getBaseURL(), item.getPureName());
        String result = null;
        try {
            result = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr&num=15";
        } catch (UnsupportedEncodingException e) {
            Logger.error(e);
        }
        return result;
    }
}
