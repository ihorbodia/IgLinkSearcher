import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProxyHelper {
    public ArrayList<ProxyModel> proxyList;

    public void InitProxyList() {
        proxyList = new ArrayList<>();
        File f = new File(".");
        f = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(".")) + File.separator + "src" + File.separator+"proxyList.txt");
        List<String> stringProxies = null;
        try {
            stringProxies = Files.readAllLines(Paths.get(f.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String str: stringProxies) {
            String[] proxy = str.split(":");
            if (proxy.length == 2){
                proxyList.add(new ProxyModel(proxy[0], proxy[1]));
            }
        }
    }
    public ProxyModel getRandomProxy()
    {
        Random rand = new Random();
        return proxyList.get(rand.nextInt(proxyList.size()));
    }
}
