import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SearchResult {
    ArrayList<SearchResultItem> Results;

    public SearchResult(Element body) {
        Results = new ArrayList<>();
        Elements items = body.select("#res");
        Elements resultDivs = items.select("div.g");
        for (Element div: resultDivs) {
            Results.add(new SearchResultItem(div));
        }
    }
}
