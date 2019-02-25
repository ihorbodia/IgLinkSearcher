package Helpers;

import Utils.RandomUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class ParseHelper {
    public static Element getQueryBody(String url, int timeOut, ProxyHelper proxyHelper, String userAgent) {
        Element doc = null;
        int i = 0;
        while (i < 5) {
            try {
                System.out.println("Processing in " + timeOut / 1000 + " seconds. Attempt: " + (i + 1) + "/3");
                Thread.sleep(timeOut);
                Connection.Response response = Jsoup.connect(url)
                        .followRedirects(true)
                        .proxy(proxyHelper.getNewProxy())
                        .userAgent(userAgent)
                        .method(Connection.Method.GET)
                        .ignoreHttpErrors(true)
                        .execute();
                doc = response.parse().body();
                if (response.statusCode() == 200) {
                    System.out.println("Success");
                    System.out.println();
                    break;
                }
                System.out.println("Response status code: " + response.statusCode());
            } catch (Exception e) {
                System.out.println("Error while request executing.");
                System.out.println(e.getMessage());
            }
            timeOut = RandomUtils.getRandomMilliseconds();
            i++;
        }
        return doc;
    }
}
