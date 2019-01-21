import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;

public class IgSearcherLogic {

    Path inputFilePath;
    Iterator<CsvItemModel> csvUserIterator;
    SearchResult results;

    int max = 80000;
    int min = 30000;

    public IgSearcherLogic() {

    }

    public void Run(){
        initCSVItems();
        while (csvUserIterator.hasNext()) {
            CsvItemModel item = csvUserIterator.next();
            Element body = getQueryBody(item);
            if (body == null) {
                continue;
            }
            System.out.println(item.CompanyName);
            results = new SearchResult(body);
            checkResultToInstagramLink(results, item);
        }
        saveCSVItems();
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

    public void saveCSVItems() {
        try (
                Writer writer = Files.newBufferedWriter(inputFilePath);
        ) {
            StatefulBeanToCsv<CsvItemModel> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(IteratorUtils.toList(csvUserIterator));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

    private void checkResultToInstagramLink(SearchResult results, CsvItemModel csvItem) {
        boolean isContains = false;
        for (SearchResultItem result: results.getResults()) {
            if (result.MainHeader.toLowerCase().contains(csvItem.URL.toLowerCase()) ||
                result.Description.toLowerCase().contains(csvItem.URL.toLowerCase()) ||
                result.SearchedLink.toLowerCase().contains(csvItem.URL.toLowerCase())) {
                isContains = true;
            }
            else if (result.MainHeader.toLowerCase().contains(csvItem.CompanyName.toLowerCase()) ||
                    result.Description.toLowerCase().contains(csvItem.CompanyName.toLowerCase()) ||
                    result.SearchedLink.toLowerCase().contains(csvItem.CompanyName.toLowerCase())) {
                isContains = true;
            }

            if (isContains) {
                csvItem.FoundedInstagram = result.SearchedLink;
            }
        }
    }

    private String createURL(CsvItemModel item) {
        String result = "";
        result += "https://www.google.com/search?q=www.instagram.com "+ item.getPureName()+" "+ item.URL+"&pws=0&gl=us&gws_rd=cr";
        return result;
    }

    private Element getQueryBody(CsvItemModel item) {
        Element doc = null;
        try {
            Random randomNum = new Random();
            int test = min + randomNum.nextInt(max);
            Thread.sleep(min + randomNum.nextInt(max));
            doc = Jsoup.connect(createURL(item))
                    .followRedirects(false)
                    .userAgent("\"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)\"")
                    .get()
                    .body();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
