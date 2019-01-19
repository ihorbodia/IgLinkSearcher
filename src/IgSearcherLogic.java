import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class IgSearcherLogic {

    Path inputFilePath;
    File inputFile;
    Iterator<CsvItemModel> csvUserIterator;


    public IgSearcherLogic() {

    }

    public void Run(){
        initCSVItems();
        while (csvUserIterator.hasNext()) {
            CsvItemModel item = csvUserIterator.next();
            Element body = getQueryBody(item);
            System.out.println(body.html());
            SearchResult results = new SearchResult(body);
            //getSearchResults(createURL(item));



            System.out.println("Link : " + item.link);
            System.out.println("==========================");
        }
    }

    private void initCSVItems() {
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CsvToBean<CsvItemModel> csvToBean = new CsvToBeanBuilder(reader)
                .withType(CsvItemModel.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        csvUserIterator = csvToBean.iterator();
    }

    private String createURL(CsvItemModel item) {
        String result = "";
        result += "https://www.google.com/search?q=www.instagram.com "+ item.getPureName()+" "+ item.link+"&pws=0&gl=us&gws_rd=cr";
        return result;
    }

    private Element getQueryBody(CsvItemModel item) {
        Element doc = null;
        try {
            doc = Jsoup.connect(createURL(item))
                    .followRedirects(false)
                    .userAgent("Mozilla/5.0")
                    .get()
                    .body();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public void Stop () {

    }

    public void restoreProperties() {

    }

    public void setInputFilePath(String path) {
        if (!StringUtils.isEmpty(path)) {
            inputFilePath = Paths.get(path);
        }
    }
}
