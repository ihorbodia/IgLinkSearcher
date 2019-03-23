package Engines;

import Models.RequestData;
import Servcies.DIResolver;
import org.jsoup.Connection;
import java.io.IOException;

abstract class WebEngine {

    final int requestDelay;
    final int requestTimeout;
    final int attemptsCount;
    final DIResolver diResolver;

    boolean isValidResponse(Connection.Response response) {
        return response != null && response.statusCode() == 200;
    }

    public WebEngine(DIResolver diResolver,  int requestDelay, int requestTimeout, int attemptsCount) {
        this.diResolver = diResolver;
        this.requestDelay = requestDelay;
        this.attemptsCount = attemptsCount;
        this.requestTimeout = requestTimeout;
    }

    protected abstract Connection.Response makeRequest(RequestData requestData) throws IOException;
}
