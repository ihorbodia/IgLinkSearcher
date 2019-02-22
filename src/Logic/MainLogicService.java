package Logic;

import Helpers.CsvHelper;
import Helpers.GuiHelper;
import Helpers.PropertiesHelper;
import Models.CsvItemModel;
import Models.SearchResult;
import Models.SearchResultItem;
import Utils.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainLogicService {

    private ArrayList<CsvItemModel> csvFileData;
    private File inputFile;

    private boolean isWorkFlag = true;
    private boolean isError = false;

    private boolean igSearch;
    private boolean twitterSearch;

    private final PropertiesHelper propertiesHelper;
    private final GuiHelper guiHelper;

    private int currentIndex = 0;

    int max = 70000;
    int min = 30000;

    public MainLogicService(PropertiesHelper propertiesHelper,  GuiHelper guiHelper) {
        this.propertiesHelper = propertiesHelper;
        this.guiHelper = guiHelper;
    }

    public void ApplicationStart() {
        String restoredPath = propertiesHelper.getSelectedInputFile();
        if (!StringUtils.isEmpty(restoredPath)) {
            if (Files.exists(Paths.get(restoredPath))) {
                guiHelper.setInputFilePath(restoredPath);
            }
        }

        if (propertiesHelper.getIsWork()) {
            boolean igSearch = propertiesHelper.getIsIgSearch();
            boolean isTwitterSearch = propertiesHelper.getIsTwitterSearch();

            guiHelper.checkInstagramSearch(igSearch);
            guiHelper.checkTwitterSearch(isTwitterSearch);
            Run();
        }
    }

    public void Run() {
        guiHelper.updateStatusText("Starting");
        Thread worker = new Thread(() -> {
            guiHelper.changeApplicationStateToWork(true);
            isError = false;
            StartWork();
            guiHelper.changeApplicationStateToWork(false);
        });
        worker.start();
    }

    private void StartWork () {
        int index = propertiesHelper.getIndex();
        csvFileData = CsvHelper.initCSVItems(inputFile);
        for (int i = index; i < csvFileData.size();  i++) {
            if (!isWorkFlag) {
                break;
            }

            propertiesHelper.saveIndex(i);
            propertiesHelper.saveIsIgSearch(igSearch);
            propertiesHelper.saveIsTwitterSearch(twitterSearch);

            currentIndex = i;
            updateStatus("");
            Element body = getQueryBody(csvFileData.get(i));
            if (body == null) {
                csvFileData.get(i).notFound = "Not found";
                CsvHelper.saveCSVItems(inputFile, csvFileData);
                continue;
            }
            System.out.println(csvFileData.get(i).companyName);
            SearchResult results = new SearchResult(body, igSearch, twitterSearch);
            updateStatus("Found: "+ results.getResults().size()+"... Parsing.");
            checkResultToInstagramLink(results, csvFileData.get(i));
            CsvHelper.saveCSVItems(inputFile, csvFileData);
        }
    }

    public void setIsIgSearch(boolean isIgSearch) {
        this.igSearch = isIgSearch;
    }

    public void setIsTwitterSearch(boolean isTwitterSearch) {
        this.twitterSearch = isTwitterSearch;
    }

    private void checkResultToInstagramLink(SearchResult results, CsvItemModel csvItem) {
        boolean isContains = false;
        if (results.getResults().size() == 0) {
            csvItem.notFound = "Not found";
            updateStatus("Result not found");
        }
        for (SearchResultItem result : results.getResults()) {
            if (result.MainHeader.toLowerCase().contains(csvItem.URL.toLowerCase()) ||
                    result.Description.toLowerCase().contains(csvItem.URL.toLowerCase()) ||
                    result.SearchedLink.toLowerCase().contains(csvItem.URL.toLowerCase())) {
                isContains = true;
            } else if (result.MainHeader.toLowerCase().replace(" ", "")
                    .contains(csvItem.URL.toLowerCase().replace(" ", "")) ||
                    result.Description.toLowerCase().replace(" ", "")
                            .contains(csvItem.URL.toLowerCase().replace(" ", "")) ||
                    result.SearchedLink.toLowerCase().replace(" ", "")
                            .contains(csvItem.URL.toLowerCase().replace(" ", ""))) {
                isContains = true;
            } else if (result.MainHeader.toLowerCase().contains(csvItem.companyName.toLowerCase()) ||
                    result.Description.toLowerCase().contains(csvItem.companyName.toLowerCase()) ||
                    result.SearchedLink.toLowerCase().contains(csvItem.companyName.toLowerCase())) {
                isContains = true;
            } else if (result.MainHeader.toLowerCase().replace(" ", "")
                    .contains(csvItem.companyName.toLowerCase().replace(" ", "")) ||
                    result.Description.toLowerCase().replace(" ", "")
                            .contains(csvItem.companyName.toLowerCase().replace(" ", "")) ||
                    result.SearchedLink.toLowerCase().replace(" ", "")
                            .contains(csvItem.companyName.toLowerCase().replace(" ", ""))) {
                isContains = true;
            } else if (result.MainHeader.toLowerCase().contains(csvItem.getPureName().toLowerCase()) ||
                    result.Description.toLowerCase().contains(csvItem.getPureName().toLowerCase()) ||
                    result.SearchedLink.toLowerCase().contains(csvItem.getPureName().toLowerCase())) {
                isContains = true;
            }

            if (isContains) {
                if (igSearch) {
                    Pattern igPattern = Pattern.compile("(((instagram\\.com\\/)|(ig\\ ?\\-\\ ?))([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?))|(@([a-z0-9_]{1,255}))");
                    Matcher igMatcher = igPattern.matcher(result.SearchedLink.toLowerCase());
                    if (igMatcher.find() && igMatcher.group(5).length() > 4) {
                        csvItem.foundInstagram = StrUtils.normalizeLink(igMatcher.group(0));
                        updateStatus("Result: " + csvItem.foundInstagram);
                    } else {
                        csvItem.notFound = "Not found";
                        updateStatus("Result not found");
                    }
                }

                if (twitterSearch) {
                    Pattern twitterPattern = Pattern.compile("((https?:\\/\\/)?(www\\.)?twitter\\.com\\/)?(t\\.co\\/)?(@|#!\\/)?([A-Za-z0-9_]{1,15})(\\/([-a-z]{1,20}))?");
                    Matcher twitterMatcher = twitterPattern.matcher(result.SearchedLink.toLowerCase());
                    if (twitterMatcher.find() && twitterMatcher.group(6).length() > 4) {
                        csvItem.foundTwitter = StrUtils.normalizeLink(twitterMatcher.group(0));
                        updateStatus("Result: " + csvItem.foundInstagram);
                    } else {
                        csvItem.notFound = "Not found";
                        updateStatus("Result not found");
                    }
                }
                break;
            }
        }
        if (StringUtils.isEmpty(csvItem.foundInstagram) && StringUtils.isEmpty(csvItem.foundTwitter)) {
            csvItem.notFound = "Not found";
        } else {
            csvItem.notFound = "";
        }
    }

    public void updateStatus(String additionalMessage) {
        if (csvFileData.size() > 1) {
           guiHelper.updateStatusText("Processed " + currentIndex + "/" + (csvFileData.size() - 1) +". "+ additionalMessage);
        }
        else {
            guiHelper.updateStatusText("Processed " + currentIndex + "/" + (csvFileData.size()) +". "+ additionalMessage);
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }


    private String createURL(CsvItemModel item) {
        String searchTerm = "";
        if (twitterSearch) {
            searchTerm = "twitter "+ item.getPureName();
        }
        if (igSearch) {
            searchTerm = "site:www.instagram.com "+item.companyName+" "+ item.getPureName() + " "+ item.URL;
        }
        try {
            searchTerm = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&pws=0&gl=us&gws_rd=cr";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return searchTerm;
    }

    private Connection.Response executeRequest(CsvItemModel item, int timeout) throws IOException, InterruptedException {
        if (!isWorkFlag) {
            return null;
        }
        System.out.println("Processing: " + timeout/1000 + " sec");
        if (timeout > (max + min)) {
            updateStatus("Waiting: " + (timeout/1000)/60 + " min");
        }else {
            updateStatus("Waiting: " + (timeout/1000) + " sec");
        }

        Thread.sleep(timeout);
        return  Jsoup.connect(createURL(item))
                .followRedirects(false)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .execute();
    }

    private Element getQueryBody(CsvItemModel item) {
        Element doc = null;
        try {
            Connection.Response response = executeRequest(item, min + new Random().nextInt(max));
            if (response != null) {
                if (response.statusCode() == 302) {
                    int triesCounter = 1;
                    while (triesCounter < 3) {
                        response = executeRequest(item, (min +(1200000 * triesCounter)) + new Random().nextInt(max + (1200000 * triesCounter)));
                        triesCounter++;
                    }
                }
                doc = response.parse().body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    public void Stop() {
        isWorkFlag = false;
        propertiesHelper.saveIndex(0);
        propertiesHelper.saveIsWork(false);
        guiHelper.updateStatusText("Stopping...");
    }

    public void setInputFilePath(File inputDataFile) {
        if (inputFile == null) {
            return;
        }
        inputFile = inputDataFile;
        propertiesHelper.saveSelectedInputFile(inputDataFile.getAbsolutePath());

    }
}
