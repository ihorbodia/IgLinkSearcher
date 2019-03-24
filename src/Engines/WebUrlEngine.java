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

    private final UserAgentsRotatorService userAgentsRotatorService;
    private final ProxyEngine proxyEngine;
    public WebUrlEngine(DIResolver diResolver, int requestDelay, int requestTimeout, int attemptsCount) {
        super(diResolver, requestDelay, requestTimeout, attemptsCount);
        this.userAgentsRotatorService = diResolver.getUserAgentsRotatorService();
        this.proxyEngine = new ProxyEngine(diResolver, 5000, 15000, 50);
    }

    public Element getWebSourceData(RequestData requestData) {
        for (int i = 1; i <= attemptsCount; i++) {
            boolean isContinueWork = diResolver.getPropertiesService().getIsWork();
            if(!isContinueWork) {
                return null;
            }
            try {
                Connection.Response response = makeRequest(requestData);
                if (isValidResponse(response)) {
                    Logger.info("Response OK from: " + requestData.requestURL);
                    return response.parse();
                }
                throw new Exception();
            } catch (Exception ex) {
                Logger.info("Attempt: "+ i);
                Logger.error("Cannot get page source, waiting for next attempt: " + requestData.requestURL +" \nCause: " + ex.getMessage());
            }
            isThreadSleep(i);
        }
        return null;
    }

    private void isThreadSleep(int currentAttempt) {
        try {
            if (currentAttempt <= attemptsCount) {
                Thread.sleep(requestDelay);
            }
        } catch (InterruptedException e) {
            Logger.error("Interrupt exception");
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
                .timeout(requestTimeout)
                .validateTLSCertificates(false)
                .execute();
    }
}
