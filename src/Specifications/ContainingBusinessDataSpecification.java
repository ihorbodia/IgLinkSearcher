package Specifications;

import Models.CsvItemModel;
import Models.SearchResultItem;
import Specifications.Abstract.AbstractSpecification;
import org.apache.commons.lang3.StringUtils;

public class ContainingBusinessDataSpecification extends AbstractSpecification<SearchResultItem> {

    private final CsvItemModel csvItemModel;
    public ContainingBusinessDataSpecification(CsvItemModel csvItemModel) {
        this.csvItemModel = csvItemModel;
    }

    @Override
    public boolean isSatisfiedBy(SearchResultItem searchResultItem) {
        String[] data = {searchResultItem.MainHeader.toLowerCase(), searchResultItem.Description.toLowerCase(), searchResultItem.SearchedLink.toLowerCase()};
        for(String searchedItemUnit : data) {
            if (StringUtils.containsAny(searchedItemUnit, csvItemModel.getURLToLower(), csvItemModel.getCompanyNameToLower(), csvItemModel.getPureNameToLower())) {
                return true;
            }
        }
        return false;
    }
}
