package Engines;

import Models.RequestData;
import Servcies.DIResolver;
import Servcies.UserAgentsRotatorService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import java.io.IOException;

public class WebUrlEngine extends WebEngine {

    private final ProxyEngine proxyEngine;
    private final UserAgentsRotatorService userAgentsRotatorService;
    private final DIResolver diResolver;

    public WebUrlEngine(DIResolver diResolver) {
        this.proxyEngine = new ProxyEngine(diResolver);
        this.userAgentsRotatorService = new UserAgentsRotatorService();
        this.diResolver = diResolver;
    }

    public Element getWebSourceData(RequestData requestData) {
        for (int i = 1; i <= attempts; i++) {
            boolean isContinueWork = diResolver.getPropertiesService().getIsWork();
            if(!isContinueWork) {
                return null;
            }
            try {
                Connection.Response response = makeRequest(requestData);
                if (isValidResponse(response)) {
                    Logger.tag("SYSTEM").info("Response OK from: " + requestData.requestURL);
                    return response.parse();
                }
                throw new Exception();
            } catch (Exception ex) {
                Logger.tag("SYSTEM").error("Cannot get page source, waiting for next attempt: " + requestData.requestURL +" \nCause: " + ex.getMessage());
            }
            isThreadSleep(i);
        }
        return null;
    }

    private void isThreadSleep(int currentAttempt) {
        try {
            if (currentAttempt <= attempts) {
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            Logger.tag("SYSTEM").error("Interrupt exception");
        }
    }

    @Override
    protected Connection.Response makeRequest(RequestData requestData) throws IOException {
        return Jsoup.connect(requestData.requestURL)
                .followRedirects(true)
                .userAgent(userAgentsRotatorService.getRandomUserAgent())
                .proxy(proxyEngine.getNewProxy())
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .validateTLSCertificates(false)
                .execute();
    }
}
