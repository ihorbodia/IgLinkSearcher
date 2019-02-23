package Models;

import org.apache.commons.lang3.StringUtils;

public class ProxyObjectDto {
    public String ip;
    public int port;

    public ProxyObjectDto(String proxyString) {
        if (!StringUtils.isEmpty(proxyString)) {
            ip = proxyString.split(":")[0];
            port = Integer.parseInt(proxyString.split(":")[1]);
        }
    }
}
