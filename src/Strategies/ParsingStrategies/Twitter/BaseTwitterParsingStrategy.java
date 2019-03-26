package Strategies.ParsingStrategies.Twitter;

import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Specifications.Abstract.AbstractSpecification;
import Specifications.ContainingBusinessDataSpecification;
import Specifications.TwitterLinksRegexSpecification;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Utils.StrUtils;
import Utils.UrlUtils;

import java.util.List;

abstract class BaseTwitterParsingStrategy extends ParsingStrategyBase {

    BaseTwitterParsingStrategy(DIResolver diResolver) {
        super(diResolver);
    }

    void getTwitterResults(CsvItemModel csvItemModel) {
        List<SearchResultItem> searchResultItems =
                getSocialMediaDataFromResults(new RequestData(UrlUtils.createURLForTwitterSearch(csvItemModel), 10, 10000));

        AbstractSpecification<SearchResultItem> twitterLinksSpecification =
                new TwitterLinksRegexSpecification().and(new ContainingBusinessDataSpecification(csvItemModel));

        SearchResultItem twitterResult = filterResults(searchResultItems, twitterLinksSpecification);
        String foundTwitter = twitterResult == null ? notFoundLabel : StrUtils.getLinkFromURL(twitterResult.SearchedLink, StrUtils.igLinkSearchPattern);
        csvItemModel.foundTwitter = StrUtils.getTwitterLinkFromURL(foundTwitter);
    }
}
