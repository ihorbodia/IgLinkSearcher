import org.apache.commons.lang3.StringUtils;

public class ProxyModel {

    public String Address;
    public int Port;

    public ProxyModel(String address, String port) {
        if (!StringUtils.isEmpty(address) && !StringUtils.isEmpty(port)) {
            Address = address;
            Port = Integer.parseInt(port);
        }
    }
}
