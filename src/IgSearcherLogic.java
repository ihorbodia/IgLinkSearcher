import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IgSearcherLogic {

    private Path inputFilePath;
    private SearchResult results;
    private List<CsvItemModel> csvFileData;
    private PropertiesHelper propertiesObject;
    private Thread worker;

    private boolean isWorkFlag = true;
    private boolean isError = false;

    int max = 60000;
    int min = 30000;

    public IgSearcherLogic(PropertiesHelper properties) {
        propertiesObject = properties;
    }

    public void Run() {
        worker = new Thread(() -> {
            changeApplicationStateToWork(true);
            StartWork();
            changeApplicationStateToWork(false);
        });
        worker.start();
    }

    private void StartWork () {
        int index = Integer.parseInt(propertiesObject.restoreProperty("index"));
        initCSVItems();
        for (int i = index; i < csvFileData.size();  i++) {
            if (!isWorkFlag) {
                break;
            }
            propertiesObject.saveProperty("index", String.valueOf(i));
            Main.gui.getLabelStatusData().setText("Processed " + i + "/" + (csvFileData.size() - 1));
            Element body = getQueryBody(csvFileData.get(i));
            if (body == null) {
                continue;
            }
            System.out.println(csvFileData.get(i).companyName);
            results = new SearchResult(body);
            checkResultToInstagramLink(results, csvFileData.get(i));
            saveCSVItems();
        }
    }

    public void changeApplicationStateToWork(boolean isWorkState) {
        Main.gui.getRunButton().setEnabled(!isWorkState);
        Main.gui.getStopButton().setEnabled(isWorkState);
        Main.gui.getSelectFileButton().setEnabled(!isWorkState);
        propertiesObject.saveProperty("isWorked", String.valueOf(isWorkState));
        isWorkFlag = true;
        isError = false;
        if (!isWorkState) {
            propertiesObject.saveProperty("index", "0");
            if (!isError) {
                Main.gui.getLabelStatusData().setText("Finished");
            }
        }
    }

    private void initCSVItems() {
        Reader reader = null;
        csvFileData = new ArrayList<>();
        try {
            reader = Files.newBufferedReader(inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CsvToBean<CsvItemModel> csvToBean = new CsvToBeanBuilder(reader)
                .withType(CsvItemModel.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        try
        {
            csvFileData.addAll(IteratorUtils.toList(csvToBean.iterator()));
        }
        catch (RuntimeException ex) {
            Main.gui.getLabelStatusData().setText("Something wrong with input file");
            isWorkFlag = false;
            isError = true;
        }
    }

    public void saveCSVItems() {
        System.out.println("saveCSVItems");
        System.out.println("csvFileData: " + csvFileData.size());
        System.out.println("inputFilepath: " + inputFilePath);
        File f = inputFilePath.toFile();
        System.out.println("inputFile: " + f.getAbsolutePath());
        if (csvFileData == null || csvFileData.size() == 0) {
            return;
        }
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(f.getAbsolutePath()))
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
            else if (result.MainHeader.toLowerCase().replace(" ", "").contains(csvItem.URL.toLowerCase().replace(" ", "")) ||
                    result.Description.toLowerCase().replace(" ", "").contains(csvItem.URL.toLowerCase().replace(" ", "")) ||
                    result.SearchedLink.toLowerCase().replace(" ", "").contains(csvItem.URL.toLowerCase().replace(" ", ""))) {
                isContains = true;
            }
            else if (result.MainHeader.toLowerCase().contains(csvItem.companyName.toLowerCase()) ||
                    result.Description.toLowerCase().contains(csvItem.companyName.toLowerCase()) ||
                    result.SearchedLink.toLowerCase().contains(csvItem.companyName.toLowerCase())) {
                isContains = true;
            }
            else if (result.MainHeader.toLowerCase().replace(" ", "").contains(csvItem.companyName.toLowerCase().replace(" ", "")) ||
                    result.Description.toLowerCase().replace(" ", "").contains(csvItem.companyName.toLowerCase().replace(" ", "")) ||
                    result.SearchedLink.toLowerCase().replace(" ", "").contains(csvItem.companyName.toLowerCase().replace(" ", ""))) {
                isContains = true;
            }
            if (isContains) {
                if (result.SearchedLink.lastIndexOf("?") > 0)
                    csvItem.foundedInstagram = result.SearchedLink.substring(0, result.SearchedLink.lastIndexOf("?"));
                else {
                    csvItem.foundedInstagram = result.SearchedLink;
                }
                break;
            }
        }
    }

    private String createURL(CsvItemModel item) {
        String result = "";
        result += "https://www.google.com/search?q=www.instagram.com " + item.getPureName() + " " + item.URL + "&pws=0&gl=us&gws_rd=cr";
        return result;
    }

    private Connection.Response executeRequest(CsvItemModel item, int timeout) throws IOException, InterruptedException {
        System.out.println("Processing: " + timeout/1000 + " sec");
        Thread.sleep(timeout);
        return  Jsoup.connect(createURL(item))
                .followRedirects(false)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
                .method(Connection.Method.GET)
                .execute();
    }

    private Element getQueryBody(CsvItemModel item) {
        Element doc = null;
        try {
            Connection.Response response = executeRequest(item, min + new Random().nextInt(max));
            if (response.statusCode() == 302) {
                int triesCounter = 1;
                while (triesCounter < 3) {
                    executeRequest(item, (min +(900000 * triesCounter)) + new Random().nextInt(max + (900000 * triesCounter)));
                    triesCounter++;
                }
            }
            doc = response.parse().body();
            System.out.println("Body: "+ doc);
        } catch (Exception e) {
            isWorkFlag = false;
            isError = true;
            e.printStackTrace();
        }
        return doc;
    }

    public void Stop() {
        isWorkFlag = false;
        propertiesObject.saveProperty("isWorked", "false");
        Main.gui.getLabelStatusData().setText("Stopping...");
    }

    public void setInputFilePath(String path) {
        if (!StringUtils.isEmpty(path)) {
            inputFilePath = Paths.get(path);
            propertiesObject.saveProperty("selectedCsvInputFile", path);
        }
    }
}
