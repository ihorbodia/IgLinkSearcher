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

    int max = 30000;
    int min = 20000;

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
        //saveCSVItems();
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
        if (csvFileData == null || csvFileData.size() == 0) {
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
                csvItem.foundedInstagram = result.SearchedLink;
            }
        }
    }

    private String createURL(CsvItemModel item) {
        String result = "";
        result += "https://www.google.com/search?q=www.instagram.com " + item.getPureName() + " " + item.URL + "&pws=0&gl=us&gws_rd=cr";
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
            isWorkFlag = false;
            isError = true;
        }
        return doc;
    }

    public void Stop() {
        isWorkFlag = false;
        Main.gui.getLabelStatusData().setText("Stopping...");
    }

    public void setInputFilePath(String path) {
        if (!StringUtils.isEmpty(path)) {
            inputFilePath = Paths.get(path);
            propertiesObject.saveProperty("selectedCsvInputFile", path);
        }
    }
}
