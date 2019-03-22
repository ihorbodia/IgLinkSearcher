package Strategies.ParsingStrategies;

import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Utils.UrlUtils;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class InstagramParsingStrategy extends ParsingStrategyBase {
    public InstagramParsingStrategy(DIResolver diResolver) {
        super(diResolver);
    }

    @Override
    public List<SearchResultItem> getSocialMediaResults(CsvItemModel csvItemModel) {
        getSocialMediaDataFromResults(new RequestData(UrlUtils.createURLForIgSearch(csvItemModel)));
        List<SearchResultItem> searchResultItems = new ArrayList<>();
        for (Element div : getElements()) {
            SearchResultItem item = new SearchResultItem(div);
            searchResultItems.add(item);
        }
        return searchResultItems;
    }
}
