package Logic;

import Helpers.CsvHelper;
import Helpers.GuiHelper;
import Helpers.ParseHelper;
import Helpers.PropertiesHelper;
import Models.CsvItemModel;
import Models.SearchResult;
import Models.SearchResultItem;
import Utils.StrUtils;
import Utils.UrlUtils;
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
            StartWork();
        }
    }

    public void StartWork () {
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

            String igSearchRequest = UrlUtils.createURLForIgSearch(csvFileData.get(i));
            Element igBody = ParseHelper.getQueryBody(igSearchRequest, new Random().nextInt(50000-20000) + 20000);

            String twitterSearchRequest = UrlUtils.createURLForIgSearch(csvFileData.get(i));
            Element twitterBody = ParseHelper.getQueryBody(twitterSearchRequest, new Random().nextInt(50000-20000) + 20000);

            if (igBody == null && twitterBody == null) {
                csvFileData.get(i).notFound = "Not found";
                CsvHelper.saveCSVItems(inputFile, csvFileData);
                continue;
            }

            System.out.println(csvFileData.get(i).companyName);
            SearchResult results = new SearchResult(igBody, twitterBody);
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
                    Pattern igPattern = Pattern.compile(StrUtils.igLinkSearchPattern);
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
                    Pattern twitterPattern = Pattern.compile(StrUtils.twitterLinkSearchPattern);
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
