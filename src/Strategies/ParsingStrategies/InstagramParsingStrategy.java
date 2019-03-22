package Strategies.ParsingStrategies;

import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Specifications.Abstract.AbstractSpecification;
import Specifications.ContainingBusinessDataSpecification;
import Specifications.IgLinksRegexSpecification;
import Utils.StrUtils;
import Utils.UrlUtils;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class InstagramParsingStrategy extends ParsingStrategyBase {

    public InstagramParsingStrategy(DIResolver diResolver) {
        super(diResolver);
    }

    @Override
    public void getSocialMediaResults(CsvItemModel csvItemModel) {
        getSocialMediaDataFromResults(new RequestData(UrlUtils.createURLForIgSearch(csvItemModel)));
        List<SearchResultItem> searchResultItems = new ArrayList<>();
        for (Element div : getElements()) {
            SearchResultItem item = new SearchResultItem(div);
            searchResultItems.add(item);
        }

        AbstractSpecification<SearchResultItem> igLinksSpecification =
                new IgLinksRegexSpecification().and(new ContainingBusinessDataSpecification(csvItemModel));

        SearchResultItem igResult = filterResults(searchResultItems, igLinksSpecification);
        csvItemModel.foundInstagram = igResult == null ? notFoundLabel : StrUtils.getLinkFromURL(igResult.SearchedLink, StrUtils.twitterLinkSearchPattern);
    }
}
