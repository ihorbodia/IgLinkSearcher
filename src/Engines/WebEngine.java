package Engines;

import Models.RequestData;
import Servcies.DIResolver;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.jsoup.Connection;
import java.io.IOException;

abstract class WebEngine {

    final int requestDelay;
    final int requestTimeout;
    final int attemptsCount;
    final DIResolver diResolver;

    final String proxyEndPoint = "zproxy.lum-superproxy.io";
    final int proxyPort = 22225;

    final String proxyUserName = "lum-customer-ihouse_d-zone-static";
    final String proxyPassword = "qs38hp672p1l";

    boolean isValidResponse(CloseableHttpResponse response) {
        return response != null && response.getStatusLine().getStatusCode() == 200;
    }

    public WebEngine(DIResolver diResolver,  int requestDelay, int requestTimeout, int attemptsCount) {
        this.diResolver = diResolver;
        this.requestDelay = requestDelay;
        this.attemptsCount = attemptsCount;
        this.requestTimeout = requestTimeout;
    }
}
