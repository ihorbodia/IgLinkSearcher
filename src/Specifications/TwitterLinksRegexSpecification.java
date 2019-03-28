package Specifications;

import Models.SearchResultItem;
import Specifications.Abstract.AbstractSpecification;
import Utils.StrUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterLinksRegexSpecification extends AbstractSpecification<SearchResultItem> {

    private final Pattern pattern = Pattern.compile(StrUtils.twitterLinkSearchPattern);

    @Override
    public synchronized boolean isSatisfiedBy(SearchResultItem searchResultItem) {
        Matcher matcher = pattern.matcher(searchResultItem.SearchedLink);
        return matcher.find();
    }
}
