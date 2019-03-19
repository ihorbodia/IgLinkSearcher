package Engines;

import ApiKeys.Keys;
import Models.RequestData;
import Servcies.DIResolver;
import Servcies.UserAgentsRotatorService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.tinylog.Logger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyEngine extends WebEngine {

    private final DIResolver diResolver;
    ProxyEngine(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    Proxy getNewProxy() {
        RequestData requestData = new RequestData(
                "http://pubproxy.com/api/proxy?google=true&last_check=3&api=" + Keys.getProxyKey() + "&format=txt&country=US,CA,UK");
        for (int i = 1; i <= attempts; i++) {
            boolean isContinueWork = diResolver.getPropertiesService().getIsWork();
            if(!isContinueWork) {
                return null;
            }
            try {
                Connection.Response response = makeRequest(requestData);
                if (isValidResponse(response)) {
                    String textProxy = response.parse().text();
                    if (!StringUtils.isEmpty(textProxy)) {
                        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(textProxy.split(":")[0], Integer.parseInt(textProxy.split(":")[1])));
                    }
                }
                throw new Exception();
            } catch (Exception ex) {
                Logger.tag("SYSTEM").error("Cannot get proxy, " + ex.getMessage() + ", waiting for next attempt.");
            }

            isThreadSleep(i);
        }
        return null;
    }

    private void isThreadSleep(int currentAttempt) {
        try {
            if (currentAttempt <= attempts) {
                Thread.sleep(requestDelay);
            }
        } catch (InterruptedException e) {
            Logger.tag("SYSTEM").error("Interrupt exception");
        }
    }

    @Override
    public Connection.Response makeRequest(RequestData requestData) throws IOException {
        return Jsoup.connect(requestData.requestURL)
                .followRedirects(true)
                .userAgent(UserAgentsRotatorService.getBotUserAgent())
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .timeout(requestDelay * 12)
                .validateTLSCertificates(false)
                .execute();
    }
}