package Engines;

import Models.RequestData;
import org.jsoup.Connection;
import java.io.IOException;

abstract class WebEngine {

    final int requestDelay = 5000;
    final int attempts = 50;

    boolean isValidResponse(Connection.Response response) {
        return response != null && response.statusCode() == 200;
    }

    protected abstract Connection.Response makeRequest(RequestData requestData) throws IOException;
}
