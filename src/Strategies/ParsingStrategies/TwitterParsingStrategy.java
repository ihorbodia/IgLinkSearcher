package Strategies.ParsingStrategies;

import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Specifications.Abstract.AbstractSpecification;
import Specifications.ContainingBusinessDataSpecification;
import Specifications.TwitterLinksRegexSpecification;
import Utils.StrUtils;
import Utils.UrlUtils;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class TwitterParsingStrategy extends ParsingStrategyBase {

    public TwitterParsingStrategy(DIResolver diResolver) {
        super(diResolver);
    }

    @Override
    public void getSocialMediaResults(CsvItemModel csvItemModel) {
        getSocialMediaDataFromResults(new RequestData(UrlUtils.createURLForTwitterSearch(csvItemModel)));
        List<SearchResultItem> searchResultItems = new ArrayList<>();
        for (Element div : getElements()) {
            SearchResultItem item = new SearchResultItem(div);
            searchResultItems.add(item);
        }

        AbstractSpecification<SearchResultItem> twitterLinksSpecification =
        new TwitterLinksRegexSpecification().and(new ContainingBusinessDataSpecification(csvItemModel));

        SearchResultItem twitterResult = filterResults(searchResultItems, twitterLinksSpecification);
        String foundTwitter = twitterResult == null ? notFoundLabel : StrUtils.getLinkFromURL(twitterResult.SearchedLink, StrUtils.igLinkSearchPattern);
        csvItemModel.foundTwitter = StrUtils.getTwitterLinkFromURL(foundTwitter);
    }
}
