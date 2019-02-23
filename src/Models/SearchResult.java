package Models;

import org.apache.commons.lang3.StringUtils;
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
        Elements items = body.select("#res");
        if (items != null) {
            Elements resultDivs = items.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem item = new SearchResultItem(div);
                if (igBody != null) {
                    if ((StringUtils.isEmpty(item.SearchedLink) || item.SearchedLink.length() > 10) &&
                            (item.SearchedLink.toLowerCase().contains("instagram.") || item.SearchedLink.toLowerCase().contains("ig.")) &&
                            !item.SearchedLink.contains("instagram.com/explore/")) {
                        Results.add(new SearchResultItem(div));
                        System.out.println("Result item: " + div);
                        System.out.println("_____________________");
                    }
                }
                if (twitterBody != null) {
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
        System.out.println("Results: " + Results.size());
        System.out.println();
    }

    public List<SearchResultItem> getResults() {
        return Results;
    }
}
