package Strategies.ParsingStrategies;

import Engines.WebUrlEngine;
import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Specifications.Abstract.Specification;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public abstract class ParsingStrategyBase {
    protected final DIResolver diResolver;
    protected String notFoundLabel = "Not found";

    protected ParsingStrategyBase(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    public abstract void getSocialMediaResults(CsvItemModel csvItemModel);

    protected List<SearchResultItem> getSocialMediaDataFromResults(RequestData requestData) {
        WebUrlEngine webUrlEngine = new WebUrlEngine(diResolver);
        Element body = webUrlEngine.getWebSourceData(requestData);

        Elements elements = null;
        if (body != null) {
            elements = body.select("#res").select("div.g");
            if (elements.size() == 0) {
                elements = body.select("#main > div[class]");
            }
            if (elements.size() == 0) {
                elements = new Elements();
            }
        }

        List<SearchResultItem> searchResultItems = new ArrayList<>();
        if (elements != null && elements.size() > 0)  {
            for (Element div : elements) {
                SearchResultItem item = new SearchResultItem(div);
                searchResultItems.add(item);
            }
        }

        return searchResultItems;
    }

    protected <T> T filterResults(List<T> set, Specification spec) {
        for(T t : set) {
            if(spec.isSatisfiedBy(t) ) {
                return t;
            }
        }
        return null;
    }
}
