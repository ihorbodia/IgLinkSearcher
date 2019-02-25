package Logic;

import Helpers.*;
import Models.CsvItemModel;
import Models.SearchResult;
import Models.SearchResultItem;
import Utils.RandomUtils;
import Utils.SearchUtils;
import Utils.StrUtils;
import Utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainLogicService {

    private ArrayList<CsvItemModel> csvFileData;
    private File inputFile;

    private boolean isWorkFlag = true;

    private boolean igSearch;
    private boolean twitterSearch;

    private final PropertiesHelper propertiesHelper;
    private final GuiHelper guiHelper;
    private final ProxyHelper proxyHelper;
    private final UserAgentRotatorHelper userAgentRotatorHelper;

    private int currentIndex = 0;

    public MainLogicService(PropertiesHelper propertiesHelper, GuiHelper guiHelper, ProxyHelper proxyHelper, UserAgentRotatorHelper userAgentRotatorHelper) {
        this.propertiesHelper = propertiesHelper;
        this.guiHelper = guiHelper;
        this.proxyHelper = proxyHelper;
        this.userAgentRotatorHelper = userAgentRotatorHelper;
    }

    public void ApplicationStart() {
        String restoredPath = propertiesHelper.getSelectedInputFile();
        if (!StringUtils.isEmpty(restoredPath)) {
            if (Files.exists(Paths.get(restoredPath))) {
                guiHelper.setInputFilePath(restoredPath);
                setInputFilePath(new File(restoredPath));
            }
        }
        igSearch = propertiesHelper.getIsIgSearch();
        twitterSearch = propertiesHelper.getIsTwitterSearch();
        guiHelper.checkInstagramSearch(igSearch);
        guiHelper.checkTwitterSearch(twitterSearch);

        if (propertiesHelper.getIsWork() && (igSearch || twitterSearch)) {
            Thread worker = new Thread(() -> {
                guiHelper.updateStatusText("Starting");
                guiHelper.changeApplicationStateToWork(true);
                StartWork();
                guiHelper.changeApplicationStateToWork(false);
                guiHelper.updateStatusText("Stopped");
            });
            worker.start();
        }
    }

    public void StartWork () {
        int index = propertiesHelper.getIndex();
        csvFileData = CsvHelper.initCSVItems(inputFile);
        if (csvFileData.size() == 0) {
            guiHelper.updateStatusText("Problem with input file");
        }
        for (int i = index; i < csvFileData.size();  i++) {
            if (!isWorkFlag) {
                break;
            }

            propertiesHelper.saveIndex(i);
            propertiesHelper.saveIsIgSearch(igSearch);
            propertiesHelper.saveIsTwitterSearch(twitterSearch);
            propertiesHelper.saveIsWork(isWorkFlag);

            currentIndex = i;
            updateStatus("");

            String igSearchRequestString = UrlUtils.createURLForIgSearch(csvFileData.get(i));
            System.out.println("Search for instagram links: " + igSearchRequestString);
            Element igBody = ParseHelper.getQueryBody(igSearchRequestString, RandomUtils.getRandomMilliseconds(), proxyHelper, userAgentRotatorHelper.getRandomUserAgent());

            String twitterSearchRequestString = UrlUtils.createURLForTwitterSearch(csvFileData.get(i));
            System.out.println("Search for twitter links: " + twitterSearchRequestString);
            Element twitterBody = ParseHelper.getQueryBody(twitterSearchRequestString,  RandomUtils.getRandomMilliseconds(), proxyHelper, userAgentRotatorHelper.getRandomUserAgent());

            if (igBody == null && twitterBody == null) {
                csvFileData.get(i).notFound = "Not found";
                CsvHelper.saveCSVItems(inputFile, csvFileData);
                continue;
            }

            System.out.println(csvFileData.get(i).companyName);
            SearchResult results = new SearchResult(igBody, twitterBody);
            updateStatus("Found instagram results: "+ results.getIgResults().size()+"... Parsing.");
            updateStatus("Found twitter results: "+ results.getTwitterResults().size()+"... Parsing.");
            checkResultToInstagramLink(results, csvFileData.get(i));
            CsvHelper.saveCSVItems(inputFile, csvFileData);
        }
    }

    public void setIsIgSearch(boolean isIgSearch) {
        this.igSearch = isIgSearch;
        disableEnableRunButton();
        propertiesHelper.saveIsIgSearch(isIgSearch);
    }

    public void setIsTwitterSearch(boolean isTwitterSearch) {
        this.twitterSearch = isTwitterSearch;
        disableEnableRunButton();
        propertiesHelper.saveIsTwitterSearch(isTwitterSearch);
    }

    private void disableEnableRunButton() {
        guiHelper.setIsEnabledRunButton((twitterSearch || igSearch));
    }

    private void checkResultToInstagramLink(SearchResult results, CsvItemModel csvItem) {
        if (results.getIgResults().size() == 0 && results.getTwitterResults().size() == 0) {
            csvItem.notFound = "Not found";
            updateStatus("Result not found");
            return;
        }

        SearchResultItem igResultItem = SearchUtils.getRightResultItem(results.getIgResults(), csvItem);
        SearchResultItem twitterResultItem = SearchUtils.getRightResultItem(results.getTwitterResults(), csvItem);

        if (igResultItem != null && igSearch) {
            csvItem.foundInstagram = SearchUtils.getSocialAccountFromString(igResultItem.SearchedLink.toLowerCase(), StrUtils.igLinkSearchPattern);
        }

        if (twitterResultItem != null && twitterSearch) {
            csvItem.foundTwitter = SearchUtils.getSocialAccountFromString(twitterResultItem.SearchedLink.toLowerCase(), StrUtils.twitterLinkSearchPattern);
        }

        if (StringUtils.isEmpty(csvItem.foundInstagram) && StringUtils.isEmpty(csvItem.foundTwitter)) {
            csvItem.notFound = "Not found";
        } else {
            csvItem.notFound = "";
        }
    }

    private void updateStatus(String additionalMessage) {
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

    public void setInputFilePath(File inputDataFile) {
        inputFile = inputDataFile;
    }
}
