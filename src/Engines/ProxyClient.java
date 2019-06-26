package Engines;

import java.io.*;
import java.util.Random;

import Models.RequestData;
import Servcies.DIResolver;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.omg.IOP.IOR;
import org.tinylog.Logger;

class ProxyClient {
    private static final String username = "lum-customer-ihouse_d-zone-static";
    private static final String password = "qs38hp672p1l";
    static final int port = 22225;
    String session_id = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
    CloseableHttpClient client;
    DIResolver diResolver;

    ProxyClient(DIResolver diResolver) {
        this.diResolver = diResolver;
        String login = username+"-session-" + session_id;

        HttpHost super_proxy = new HttpHost("zproxy.lum-superproxy.io", port);
        CredentialsProvider cred_provider = new BasicCredentialsProvider();
        cred_provider.setCredentials(new AuthScope(super_proxy),
                new UsernamePasswordCredentials(login, password));
        client = HttpClients.custom()
                .setConnectionManager(new BasicHttpClientConnectionManager())
                .setProxy(super_proxy)
                .setDefaultCredentialsProvider(cred_provider)
                .build();
    }

    synchronized Element request(RequestData requestData) {
        for (int i = 1; i <= requestData.attemptsCount; i++) {
            boolean isContinueWork = diResolver.getPropertiesService().getIsWork();
            if (!isContinueWork) {
                return null;
            }
            HttpGet request = new HttpGet(requestData.requestURL);
            try (CloseableHttpResponse response = client.execute(request)) {
                if (isValidResponse(response)) {
                    Logger.info("Response OK from: " + requestData.requestURL);
                    return Jsoup.parse(EntityUtils.toString(response.getEntity()));
                }
            } catch (Exception ex) {
                Logger.error("Attempt: " + i + "\nCannot get page source, waiting for next attempt: " + requestData.requestURL + " \nCause: " + ex.getMessage());
            } finally {
                isThreadSleep(i, requestData);
            }
        }
        return null;
    }

    private void isThreadSleep(int currentAttempt, RequestData requestData) {
        try {
            if (currentAttempt <= requestData.attemptsCount) {
                Thread.sleep(requestData.requestDelay);
            }
        } catch (InterruptedException e) {
            Logger.error("Interrupt exception");
        }
    }

    private boolean isValidResponse(CloseableHttpResponse response) throws IOException {
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            return true;
        } else {
            throw new IOException(response != null ? response.getStatusLine().toString() : null);
        }
    }

    void close() throws IOException { client.close(); }
}