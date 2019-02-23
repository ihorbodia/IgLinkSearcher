package Models;

import Utils.SearchUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    List<SearchResultItem> Results;

    public SearchResult(Element igBody, Element twitterBody) {
        if (igBody == null || igBody.text().toLowerCase().contains("The document has moved")) {
            System.out.println("Instagram result body is null");
        }
        if (twitterBody == null || twitterBody.text().toLowerCase().contains("The document has moved")) {
            System.out.println("Twitter body is null");
        }

        Results = new ArrayList<>();

        Elements igResultsItems = igBody != null ? igBody.select("#res") : null;
        if (igResultsItems != null) {
            Elements resultDivs = igResultsItems.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem item = new SearchResultItem(div);
                if (SearchUtils.isHasInstagramSearchResultCriteria(item.SearchedLink)) {
                    Results.add(new SearchResultItem(div));
                    System.out.println("Result item: " + div);
                    System.out.println("_____________________");
                }
            }
        }

        Elements twitterResultsItems = twitterBody != null ? twitterBody.select("#res") : null;
        if (twitterResultsItems != null) {
            Elements resultDivs = twitterResultsItems.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem item = new SearchResultItem(div);
                if (SearchUtils.isHasTwitterSearchResultCriteria(item.SearchedLink)) {
                    Results.add(new SearchResultItem(div));
                    System.out.println("Result item: " + div);
                    System.out.println("_____________________");
                }
            }
        }
        System.out.println("Results: " + Results.size());
        System.out.println();
    }

    public List<SearchResultItem> getResults() {
        return Results;
    }
}
