//import Helpers.CsvHelper;
//import Helpers.PropertiesHelper;
//import Models.CsvItemModel;
//import Models.SearchResult;
//import Models.SearchResultItem;
//import Utils.StrUtils;
//import com.opencsv.CSVWriter;
//import com.opencsv.bean.CsvToBean;
//import com.opencsv.bean.CsvToBeanBuilder;
//import com.opencsv.bean.StatefulBeanToCsv;
//import com.opencsv.bean.StatefulBeanToCsvBuilder;
//import com.opencsv.enums.CSVReaderNullFieldIndicator;
//import com.opencsv.exceptions.CsvDataTypeMismatchException;
//import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
//import org.apache.commons.collections4.IteratorUtils;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Element;
//
//import java.io.*;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class IgSearcherLogic {
//
//    private ArrayList<CsvItemModel> csvFileData;
//    private PropertiesHelper propertiesObject;
//    private File inputFile;
//
//    private boolean isWorkFlag = true;
//    private boolean isError = false;
//
//    private boolean igSearch;
//    private boolean twitterSearch;
//
//    int max = 70000;
//    int min = 30000;
//
//    public IgSearcherLogic(PropertiesHelper properties) {
//        propertiesObject = properties;
//    }
//
//    public void Run() {
//        Main.gui.getLabelStatusData().setText("Starting...");
//        Thread worker = new Thread(() -> {
//            changeApplicationStateToWork(true);
//            isError = false;
//            StartWork();
//            changeApplicationStateToWork(false);
//        });
//        worker.start();
//    }
//
//
//
//    private void StartWork () {
//        int index = Integer.parseInt(propertiesObject.restoreProperty("index"));
//        csvFileData = CsvHelper.initCSVItems(inputFile);
//        for (int i = index; i < csvFileData.size();  i++) {
//            if (!isWorkFlag) {
//                break;
//            }
//
//            PropertiesHelper.saveIndex(i);
//            PropertiesHelper.saveIsIgSearch(isIgSearch());
//            PropertiesHelper.saveIsTwitterSearch(isTwitterSearch());
//
//            updateStatus("");
//            Element body = getQueryBody(csvFileData.get(i));
//            if (body == null) {
//                csvFileData.get(i).notFound = "Not found";
//                CsvHelper.saveCSVItems(inputFile, csvFileData);
//                continue;
//            }
//            System.out.println(csvFileData.get(i).companyName);
//            SearchResult results = new SearchResult(body, isIgSearch(), isTwitterSearch());
//            updateStatus("Found: "+ results.getResults().size()+"... Parsing.");
//            checkResultToInstagramLink(results, csvFileData.get(i));
//            CsvHelper.saveCSVItems(inputFile, csvFileData);
//        }
//    }
//
//    public void changeApplicationStateToWork(boolean isWorkState) {
//        Main.gui.getRunButton().setEnabled(!isWorkState);
//        Main.gui.getStopButton().setEnabled(isWorkState);
//        Main.gui.getSelectFileButton().setEnabled(!isWorkState);
//        Main.gui.getIgRadioButton().setEnabled(!isWorkState);
//        Main.gui.getTwitterRadioButton().setEnabled(!isWorkState);
//        PropertiesHelper.saveIsWork(isWorkState);
//        isWorkFlag = true;
//        if (!isWorkState) {
//            PropertiesHelper.saveIndex(0);
//            if (!isError) {
//                Main.gui.getLabelStatusData().setText("Finished");
//            }
//        }
//    }
//
//    private void checkResultToInstagramLink(SearchResult results, CsvItemModel csvItem) {
//        boolean isContains = false;
//        if (results.getResults().size() == 0) {
//            csvItem.notFound = "Not found";
//            updateStatus("Result not found");
//        }
//        for (SearchResultItem result : results.getResults()) {
//            if (result.MainHeader.toLowerCase().contains(csvItem.URL.toLowerCase()) ||
//                    result.Description.toLowerCase().contains(csvItem.URL.toLowerCase()) ||
//                    result.SearchedLink.toLowerCase().contains(csvItem.URL.toLowerCase())) {
//                isContains = true;
//            } else if (result.MainHeader.toLowerCase().replace(" ", "")
//                    .contains(csvItem.URL.toLowerCase().replace(" ", "")) ||
//                    result.Description.toLowerCase().replace(" ", "")
//                            .contains(csvItem.URL.toLowerCase().replace(" ", "")) ||
//                    result.SearchedLink.toLowerCase().replace(" ", "")
//                            .contains(csvItem.URL.toLowerCase().replace(" ", ""))) {
//                isContains = true;
//            } else if (result.MainHeader.toLowerCase().contains(csvItem.companyName.toLowerCase()) ||
//                    result.Description.toLowerCase().contains(csvItem.companyName.toLowerCase()) ||
//                    result.SearchedLink.toLowerCase().contains(csvItem.companyName.toLowerCase())) {
//                isContains = true;
//            } else if (result.MainHeader.toLowerCase().replace(" ", "")
//                    .contains(csvItem.companyName.toLowerCase().replace(" ", "")) ||
//                    result.Description.toLowerCase().replace(" ", "")
//                            .contains(csvItem.companyName.toLowerCase().replace(" ", "")) ||
//                    result.SearchedLink.toLowerCase().replace(" ", "")
//                            .contains(csvItem.companyName.toLowerCase().replace(" ", ""))) {
//                isContains = true;
//            } else if (result.MainHeader.toLowerCase().contains(csvItem.getPureName().toLowerCase()) ||
//                    result.Description.toLowerCase().contains(csvItem.getPureName().toLowerCase()) ||
//                    result.SearchedLink.toLowerCase().contains(csvItem.getPureName().toLowerCase())) {
//                isContains = true;
//            }
//
//            if (isContains) {
//                if (isIgSearch()) {
//                    Pattern igPattern = Pattern.compile("(((instagram\\.com\\/)|(ig\\ ?\\-\\ ?))([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?))|(@([a-z0-9_]{1,255}))");
//                    Matcher igMatcher = igPattern.matcher(result.SearchedLink.toLowerCase());
//                    if (igMatcher.find() && igMatcher.group(5).length() > 4) {
//                        csvItem.foundInstagram = StrUtils.normalizeLink(igMatcher.group(0));
//                        updateStatus("Result: " + csvItem.foundInstagram);
//                    } else {
//                        csvItem.notFound = "Not found";
//                        updateStatus("Result not found");
//                    }
//                }
//
//                if (isTwitterSearch()) {
//                    Pattern twitterPattern = Pattern.compile("((https?:\\/\\/)?(www\\.)?twitter\\.com\\/)?(t\\.co\\/)?(@|#!\\/)?([A-Za-z0-9_]{1,15})(\\/([-a-z]{1,20}))?");
//                    Matcher twitterMatcher = twitterPattern.matcher(result.SearchedLink.toLowerCase());
//                    if (twitterMatcher.find() && twitterMatcher.group(6).length() > 4) {
//                        csvItem.foundTwitter = StrUtils.normalizeLink(twitterMatcher.group(0));
//                        updateStatus("Result: " + csvItem.foundInstagram);
//                    } else {
//                        csvItem.notFound = "Not found";
//                        updateStatus("Result not found");
//                    }
//                }
//                break;
//            }
//        }
//        if (StringUtils.isEmpty(csvItem.foundInstagram) && StringUtils.isEmpty(csvItem.foundTwitter)) {
//                csvItem.notFound = "Not found";
//        } else {
//            csvItem.notFound = "";
//        }
//    }
//
//
//    private String createURL(CsvItemModel item) {
//        String searchTerm = "";
//        if (isTwitterSearch()) {
//            searchTerm = "twitter "+ item.getPureName();
//        }
//        if (isIgSearch()) {
//            searchTerm = "site:www.instagram.com "+item.companyName+" "+ item.getPureName() + " "+ item.URL;
//        }
//        try {
//            searchTerm = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr";
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return searchTerm;
//    }
//
//    private Connection.Response executeRequest(CsvItemModel item, int timeout) throws IOException, InterruptedException {
//        if (!isWorkFlag) {
//            return null;
//        }
//        System.out.println("Processing: " + timeout/1000 + " sec");
//        if (timeout > (max + min)) {
//            updateStatus("Waiting: " + (timeout/1000)/60 + " min");
//        }else {
//            updateStatus("Waiting: " + (timeout/1000) + " sec");
//        }
//
//        Thread.sleep(timeout);
//        return  Jsoup.connect(createURL(item))
//                .followRedirects(false)
//                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
//                .method(Connection.Method.GET)
//                .ignoreHttpErrors(true)
//                .execute();
//    }
//
//    private Element getQueryBody(CsvItemModel item) {
//        Element doc = null;
//        try {
//            Connection.Response response = executeRequest(item, min + new Random().nextInt(max));
//            if (response != null) {
//                if (response.statusCode() == 302) {
//                    int triesCounter = 1;
//                    while (triesCounter < 3) {
//                        response = executeRequest(item, (min +(1200000 * triesCounter)) + new Random().nextInt(max + (1200000 * triesCounter)));
//                        triesCounter++;
//                    }
//                }
//                doc = response.parse().body();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return doc;
//    }
//
//    public void Stop() {
//        isWorkFlag = false;
//        PropertiesHelper.saveIndex(0);
//        PropertiesHelper.saveIsWork(false);
//        Main.gui.getLabelStatusData().setText("Stopping...");
//    }
//
//    public void setInputFilePath(String path) {
//        if (!StringUtils.isEmpty(path)) {
//            inputFile = new File(path);
//            PropertiesHelper.saveSelectedInputFile(path);
//        }
//    }
//
//    public boolean isIgSearch() {
//        return igSearch;
//    }
//
//    public void setIgSearch(boolean igSearch) {
//        this.igSearch = igSearch;
//    }
//
//    public boolean isTwitterSearch() {
//        return twitterSearch;
//    }
//
//    public void setTwitterSearch(boolean twitterSearch) {
//        this.twitterSearch = twitterSearch;
//    }
//}
