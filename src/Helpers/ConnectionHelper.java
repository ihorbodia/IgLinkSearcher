package Helpers;

import Models.ProxyObjectDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

class ConnectionHelper {

    static Connection.Response executeRequest(String url, ProxyObjectDto proxy, String userAgent) throws IOException {
        return Jsoup.connect(url)
                .followRedirects(true)
                .proxy(proxy.ip, proxy.port)
                .userAgent(userAgent)
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .execute();
    }
}
