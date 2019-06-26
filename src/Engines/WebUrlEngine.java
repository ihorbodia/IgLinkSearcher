package Engines;

import Models.RequestData;
import Servcies.DIResolver;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

import java.io.IOException;

public class WebUrlEngine extends WebEngine {


    public WebUrlEngine(DIResolver diResolver) {
        super(diResolver);

    }
    public synchronized Element getWebSourceData(RequestData requestData) {
        ProxyClient proxy = new ProxyClient(diResolver);
        Element el = proxy.request(requestData);
        try {
            proxy.close();
        } catch (IOException e) {
            Logger.error(e);
        }
        return el;
    }
}
