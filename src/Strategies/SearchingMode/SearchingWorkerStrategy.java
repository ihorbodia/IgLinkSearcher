package Strategies.SearchingMode;

import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Specifications.Abstract.AbstractSpecification;
import Specifications.Abstract.Specification;
import Specifications.ContainingBusinessDataSpecification;
import Specifications.IgLinksRegexSpecification;
import Specifications.TwitterLinksRegexSpecification;
import Strategies.ParsingStrategies.InstagramParsingStrategy;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Strategies.ParsingStrategies.TwitterParsingStrategy;
import Servcies.DIResolver;
import Servcies.GuiService;
import Servcies.InputDataService;
import Utils.StrUtils;
import Utils.UrlUtils;

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
        List<CsvItemModel> csvFileData = diResolver.getInputDataService().getInputCsvModelItems();
        int size = diResolver.getInputDataService().getInputCsvModelItems().size();

        ParsingStrategyBase instagramParsingStrategy = new InstagramParsingStrategy(diResolver);
        ParsingStrategyBase twitterParsingStrategy = new TwitterParsingStrategy(diResolver);

        String notFoundLabel = "Not found";

        for (int i = index; i < size;  i++) {
            if (!isWork) {
                break;
            }
            guiService.updateStatusText("Processing started");

            CsvItemModel currentCsvItemModel = csvFileData.get(i);

            List<SearchResultItem> igResults = instagramParsingStrategy.getSocialMediaResults(currentCsvItemModel);
            List<SearchResultItem> twitterResults = twitterParsingStrategy.getSocialMediaResults(currentCsvItemModel);

            AbstractSpecification<SearchResultItem> igLinksSpecification =
                    new IgLinksRegexSpecification().and(new ContainingBusinessDataSpecification(currentCsvItemModel));

            AbstractSpecification<SearchResultItem> twitterLinksSpecification =
                    new TwitterLinksRegexSpecification().and(new ContainingBusinessDataSpecification(currentCsvItemModel));

            SearchResultItem igResult = filterResults(igResults, igLinksSpecification);
            SearchResultItem twitterResult = filterResults(twitterResults, twitterLinksSpecification);

            currentCsvItemModel.foundInstagram = igResult == null ? notFoundLabel : StrUtils.getLinkFromURL(igResult.SearchedLink, StrUtils.igLinkSearchPattern);
            currentCsvItemModel.foundTwitter = twitterResult == null ? notFoundLabel : StrUtils.getLinkFromURL(twitterResult.SearchedLink, StrUtils.twitterLinkSearchPattern);

            guiService.updateStatusText(String.format("Processed %d\\%d items", i, size));
            inputDataService.updateResultCsvItems();
        }
    }

    private <T> T filterResults(List<T> set, Specification spec) {
        for(T t : set) {
            if(spec.isSatisfiedBy(t) ) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void stopProcessing() {
        isWork = false;
    }
}
