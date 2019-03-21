package Strategies.SearchingMode;

import Models.CsvItemModel;
import Models.SearchResultItem;
import Strategies.ParsingStrategies.InstagramParsingStrategy;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Strategies.ParsingStrategies.TwitterParsingStrategy;
import Servcies.DIResolver;
import Servcies.GuiService;
import Servcies.InputDataService;
import Utils.SearchUtils;
import Utils.StrUtils;
import org.apache.commons.lang3.StringUtils;

import javax.naming.directory.SearchResult;
import java.util.List;

public class SearchingWorkerStrategy extends SearchModeStrategyBase {
    public SearchingWorkerStrategy(DIResolver diResolver) {
        this.diResolver = diResolver;
        this.guiService = diResolver.getGuiService();
        this.inputDataService = diResolver.getInputDataService();
    }

    private final DIResolver diResolver;
    private final GuiService guiService;
    private final InputDataService inputDataService;
    private boolean isWork = true;

    @Override
    public void processData(DIResolver diResolver) {
        int index = diResolver.getPropertiesService().getIndex();
        GuiService guiService = diResolver.getGuiService();
        List<CsvItemModel> csvFileData = diResolver.getInputDataService().getInputCsvModelItems();
        int size = diResolver.getInputDataService().getInputCsvModelItems().size();

        for (int i = index; i < size;  i++) {
            if (!isWork) {
                break;
            }

            ParsingStrategyBase instagramParsingStrategy = new InstagramParsingStrategy(diResolver);
            ParsingStrategyBase twitterParsingStrategy = new TwitterParsingStrategy(diResolver);

            instagramParsingStrategy.getSocialMediaResults(csvFileData.get(i));
            twitterParsingStrategy.getSocialMediaResults(csvFileData.get(i));

            if (igBody == null && twitterBody == null) {
                csvFileData.get(i).notFound = "Not found";
                inputDataService.updateResultCsvItems();
                continue;
            }

            System.out.println(csvFileData.get(i).companyName);
            checkResultToLinks(results, csvFileData.get(i));
            /*REFACTORING*/

            inputDataService.updateResultCsvItems();
        }
    }

    private void checkResultToLinks(SearchResult results, CsvItemModel csvItem) {
        if (results.getIgResults().size() == 0 && results.getTwitterResults().size() == 0) {
            csvItem.notFound = "Not found";
            guiService.updateStatusText("Result not found");
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

    @Override
    public void stopProcessing() {
        isWork = false;
    }
}
