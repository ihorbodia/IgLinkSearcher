package Helpers;

import Models.ProxyObjectDto;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;

public class ParseHelper {
    public static Element getQueryBody(String url, int timeOut, ProxyObjectDto proxy, String userAgent) {
        Element doc = null;
        try {
            System.out.println("Processing: " + timeOut / 1000 + " sec");
            Thread.sleep(timeOut);
            Connection.Response response = ConnectionHelper.executeRequest(url, proxy, userAgent);
            if (response != null && response.statusCode() == 302) {
                int triesCounter = 1;
                while (triesCounter < 3) {
                    response = ConnectionHelper.executeRequest(url, proxy, userAgent);
                    triesCounter++;
                }
                doc = response.parse().body();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return doc;
    }
}
