package Strategies.ParsingStrategies;

import Models.CsvItemModel;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Utils.SearchUtils;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class InstagramParsingStrategy extends ParsingStrategyBase {
    public InstagramParsingStrategy(DIResolver diResolver) {
        super(diResolver);
    }

    @Override
    public List<SearchResultItem> getSocialMediaResults(CsvItemModel csvItemModel) {
        getSocialMediaDataFromResults(csvItemModel);
        List<SearchResultItem> searchResultItems = new ArrayList<>();
        for (Element div : getElements()) {
            SearchResultItem item = new SearchResultItem(div);
            if (SearchUtils.isHasInstagramSearchResultCriteria(item.SearchedLink)) {
                searchResultItems.add(item);
            }
        }
        return searchResultItems;
    }
}
