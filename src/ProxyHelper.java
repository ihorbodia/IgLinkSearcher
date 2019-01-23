import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProxyHelper {
    public ArrayList<Proxy> proxyList;

    public void InitProxyList() {
        try {
            List<String> stringProxies = Files.readAllLines(Paths.get("proxyList.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
