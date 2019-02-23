package Helpers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ConnectionHelper {

    public static Connection.Response executeRequest(String url) throws IOException {
        return Jsoup.connect(url)
                .followRedirects(false)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .execute();
    }
}
