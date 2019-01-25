import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    List<SearchResultItem> Results;

    public SearchResult(Element body) {
        if (body == null || body.text().toLowerCase().contains("The document has moved")){
            System.out.println("Body is null");
        }
        Results = new ArrayList<>();
        Elements items = body.select("#res");
        if (items != null) {
            Elements resultDivs = items.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem item = new SearchResultItem(div);
                if (item.SearchedLink.toLowerCase().contains("instagram.") || item.SearchedLink.toLowerCase().contains("ig.")) {
                    Results.add(new SearchResultItem(div));
                    System.out.println("Result item: " + div);
                    System.out.println("_____________________");
                }
            }
        }
        System.out.println("Results: "+Results.size());
        System.out.println();
    }

    public  List<SearchResultItem> getResults() {
        return Results;
    }
}
