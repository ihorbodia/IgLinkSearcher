package Models.Strategies.ParsingStrategies;

import Engines.WebUrlEngine;
import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Utils.SearchUtils;
import Utils.UrlUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public abstract class ParsingStrategyBase {
    private final DIResolver diResolver;
    private List<SearchResultItem> resultsContainer;

    public ParsingStrategyBase(DIResolver diResolver) {
        this.diResolver = diResolver;
        resultsContainer = new ArrayList<>();
    }

    public List<SearchResultItem> getSocialMediaDataFromResults(CsvItemModel csvItemModel) {
        WebUrlEngine webUrlEngine = new WebUrlEngine(diResolver);
        RequestData requestData = new RequestData(UrlUtils.createURLForIgSearch(csvItemModel));
        Element igBody = webUrlEngine.getWebSourceData(requestData);

        Elements igResultsItems = igBody != null ? igBody.select("#res") : null;
        Elements resultDivs = igResultsItems == null ? new Elements() : igResultsItems.select("div.g");
        for (Element div : resultDivs) {
            SearchResultItem item = new SearchResultItem(div);
            if (SearchUtils.isHasInstagramSearchResultCriteria(item.SearchedLink)) {
                resultsContainer.add(item);
            }
        }
        return resultsContainer;
    }
}
