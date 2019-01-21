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

import javax.swing.*;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class IgSearcherLogic {

    Path inputFilePath;
    SearchResult results;
    List<CsvItemModel> csvFileData;

    ExecutorService executorService;
    public Future<?> future;

    int max = 80000;
    int min = 30000;

    public IgSearcherLogic() {

    }

    public void Run(JLabel statusLabelData, JButton startButton, JButton stopButton){
        Thread worker = new Thread(() -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            StartWork(statusLabelData);
            while (!future.isDone()) {
            }
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
        worker.start();
    }

    private void StartWork (JLabel statusLabelData) {
        executorService = Executors.newSingleThreadExecutor();
        future = executorService.submit(() -> {
            initCSVItems();
            statusLabelData.setText("Scraped: 0"+"/" + csvFileData.size());
            int counter = 0;
            for (CsvItemModel item : csvFileData) {
                Element body = getQueryBody(item);
                if (body == null) {
                    continue;
                }
                counter++;
                statusLabelData.setText("Scraped: " +counter+ "/" + csvFileData.size());
                System.out.println(item.companyName);
                results = new SearchResult(body);
                checkResultToInstagramLink(results, item);

            }
            saveCSVItems();
        });
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

        csvFileData = IteratorUtils.toList(csvToBean.iterator());
    }

    public void saveCSVItems() {
        if (csvFileData.size() == 0) {
            return;
        }
        try (
                Writer writer = Files.newBufferedWriter(inputFilePath)
        ) {
            StatefulBeanToCsv<CsvItemModel> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();
            beanToCsv.write(csvFileData);
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
            else if (result.MainHeader.toLowerCase().contains(csvItem.companyName.toLowerCase()) ||
                    result.Description.toLowerCase().contains(csvItem.companyName.toLowerCase()) ||
                    result.SearchedLink.toLowerCase().contains(csvItem.companyName.toLowerCase())) {
                isContains = true;
            }

            if (isContains) {
                csvItem.foundedInstagram = result.SearchedLink;
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
        future.cancel(true);
        saveCSVItems();
    }

    public void restoreProperties() {

    }

    public void setInputFilePath(String path) {
        if (!StringUtils.isEmpty(path)) {
            inputFilePath = Paths.get(path);
        }
    }
}
