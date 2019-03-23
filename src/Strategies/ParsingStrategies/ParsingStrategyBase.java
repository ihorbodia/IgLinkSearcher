package Strategies.ParsingStrategies;

import Engines.WebUrlEngine;
import Models.CsvItemModel;
import Models.RequestData;
import Models.SearchResultItem;
import Servcies.DIResolver;
import Specifications.Abstract.Specification;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;

public abstract class ParsingStrategyBase {
    private final DIResolver diResolver;
    private Elements elements;
    String notFoundLabel = "Not found";

    ParsingStrategyBase(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    public abstract void getSocialMediaResults(CsvItemModel csvItemModel);

    void getSocialMediaDataFromResults(RequestData requestData) {
        WebUrlEngine webUrlEngine = new WebUrlEngine(diResolver, 15000, 20000, 10);
        Element body = webUrlEngine.getWebSourceData(requestData);

        elements = body != null ? body.select("#res").select("div.g") : new Elements();
    }

    <T> T filterResults(List<T> set, Specification spec) {
        for(T t : set) {
            if(spec.isSatisfiedBy(t) ) {
                return t;
            }
        }
        return null;
    }

    Elements getElements() {
        return elements;
    }
}
