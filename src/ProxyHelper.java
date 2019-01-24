import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProxyHelper {
    String Ip;
    String Port;

    public void rotateProxy() throws IOException {
        String serviceUri = "http://falcon.proxyrotator.com:51337/?apiKey=gxfZBHDk3AMcCEapyjYToerSzVXshqLd&get=true";
        String jsonString = "";
        String proxyAddress = "";

        URL iurl = new URL(serviceUri);
        HttpURLConnection request = (HttpURLConnection)iurl.openConnection();
        request.connect();

        String jsonStringResponse = request.getResponseMessage();

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        ProxyObjResponse proxyObj = gson.fromJson(jsonStringResponse, ProxyObjResponse.class);


//
//        using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
//        using (Stream stream = response.GetResponseStream())
//        using (StreamReader reader = new StreamReader(stream))
//        {
//            jsonString = reader.ReadToEnd();
//
//            //the following class 'prxy' is the object from the json response from proxy rotator
//            prxy px = JsonConvert.DeserializeObject(jsonString, typeof(prxy)) as prxy;
//            proxyAddress = "http://" + px.proxy + "/";
//        }
//
//        /*****************************************/
//        /* This part registers the proxy for use */
//        /*****************************************/
//        WebProxy proxyObject = new WebProxy(proxyAddress);
//        WebRequest.DefaultWebProxy = proxyObject;
//        /**********************/
//        /*  END proxy rotator */
//        /**********************/
    }

}
