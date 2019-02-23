package Helpers;

import Models.ProxyObjectDto;
import Utils.StrUtils;
import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ProxyHelper {

    private GuiHelper guiHelper;
    public ProxyHelper(GuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    public ProxyObjectDto getNewProxy() {
        Connection.Response response = null;
        String json = null;

        boolean success = false;

        while(!success) {
            try {
                response = Jsoup.connect("http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt")
                        .ignoreContentType(true)
                        .userAgent("DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)")
                        .method(Connection.Method.GET)
                        .ignoreHttpErrors(true)
                        .timeout(10000)
                        .execute();

                json = response.parse().text();
                //logService.LogMessage("Response from proxy service: " + json);
                if (StrUtils.isProxyGrabbed(json)) {
                    success = true;
                    break;
                }
            } catch (IOException ex) {
                //logService.LogMessage("Cannot get proxy");
            }
            try {
                //logService.LogMessage("Waiting 5 minutes to get proxy again...");
                guiHelper.updateStatusText("Waiting 5 minutes to get proxy again...");
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                //logService.LogMessage("Thread sleep 300000 problem");
            }
            success = false;
        }

        if(success) {
            return new ProxyObjectDto(json);
        }
        return null;
    }
}
