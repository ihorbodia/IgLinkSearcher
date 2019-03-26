package Strategies.ParsingStrategies.Instagram;

import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Specifications.Abstract.AbstractSpecification;
import Specifications.ContainingBusinessDataSpecification;
import Specifications.IgLinksRegexSpecification;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Utils.StrUtils;
import Utils.UrlUtils;

import java.util.List;

abstract class BaseInstagramParsingStrategy extends ParsingStrategyBase {

    BaseInstagramParsingStrategy(DIResolver diResolver) {
        super(diResolver);
    }

    void getIgResults(CsvItemModel csvItemModel) {
        List<SearchResultItem> searchResultItems =
                getSocialMediaDataFromResults(new RequestData(UrlUtils.createURLForIgSearch(csvItemModel), 10, 10000));

        AbstractSpecification<SearchResultItem> igLinksSpecification =
                new IgLinksRegexSpecification().and(new ContainingBusinessDataSpecification(csvItemModel));

        SearchResultItem igResult = filterResults(searchResultItems, igLinksSpecification);
        String foundInstagram = igResult == null ? notFoundLabel : StrUtils.getLinkFromURL(igResult.SearchedLink, StrUtils.twitterLinkSearchPattern);
        csvItemModel.foundInstagram = StrUtils.getInstagramLinkFromURL(foundInstagram);
    }
}
