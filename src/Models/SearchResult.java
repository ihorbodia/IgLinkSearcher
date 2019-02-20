package Models;

import Models.SearchResultItem;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    List<SearchResultItem> Results;
    private boolean isIgSearch;
    private boolean isTwitterSearch;

    public SearchResult(Element body, boolean isIgSearch, boolean isTwitterSearch) {
        this.isIgSearch = isIgSearch;
        this.isTwitterSearch = isTwitterSearch;
        if (body == null || body.text().toLowerCase().contains("The document has moved")){
            System.out.println("Body is null");
        }
        Results = new ArrayList<>();
        Elements items = body.select("#res");
        if (items != null) {
            Elements resultDivs = items.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem item = new SearchResultItem(div);
                if (isIgSearch) {
                    if ((StringUtils.isEmpty(item.SearchedLink) || item.SearchedLink.length() > 10) &&
                            (item.SearchedLink.toLowerCase().contains("instagram.") || item.SearchedLink.toLowerCase().contains("ig.")) &&
                            !item.SearchedLink.contains("instagram.com/explore/")) {
                        Results.add(new SearchResultItem(div));
                        System.out.println("Result item: " + div);
                        System.out.println("_____________________");
                    }
                }
                if (isTwitterSearch) {
                    if ((StringUtils.isEmpty(item.SearchedLink) || item.SearchedLink.length() > 10) &&
                            (item.SearchedLink.toLowerCase().contains("twitter.") || item.SearchedLink.toLowerCase().contains("t.co")) &&
                            !item.SearchedLink.contains("twitter.com/hashtag/")) {
                        Results.add(new SearchResultItem(div));
                        System.out.println("Result item: " + div);
                        System.out.println("_____________________");
                    }
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
