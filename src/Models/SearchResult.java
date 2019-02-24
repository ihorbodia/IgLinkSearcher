package Models;

import Utils.SearchUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<SearchResultItem> IgResults;
    private List<SearchResultItem> TwitterResults;

    public SearchResult(Element igBody, Element twitterBody) {
        if (igBody == null || igBody.text().toLowerCase().contains("The document has moved")) {
            System.out.println("Instagram result body is null");
        }
        if (twitterBody == null || twitterBody.text().toLowerCase().contains("The document has moved")) {
            System.out.println("Twitter body is null");
        }

        IgResults = new ArrayList<>();
        Elements igResultsItems = igBody != null ? igBody.select("#res") : null;
        if (igResultsItems != null) {
            Elements resultDivs = igResultsItems.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem item = new SearchResultItem(div);
                if (SearchUtils.isHasInstagramSearchResultCriteria(item.SearchedLink)) {
                    IgResults.add(item);
                }
            }
        }

        TwitterResults = new ArrayList<>();
        Elements twitterResultsItems = twitterBody != null ? twitterBody.select("#res") : null;
        if (twitterResultsItems != null) {
            Elements resultDivs = twitterResultsItems.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem item = new SearchResultItem(div);
                if (SearchUtils.isHasTwitterSearchResultCriteria(item.SearchedLink)) {
                    TwitterResults.add(item);
                }
            }
        }
        System.out.println("Ig results: " + IgResults.size());
        System.out.println("Twitter results: " + TwitterResults.size());
        System.out.println();
    }

    public List<SearchResultItem> getIgResults() {
        return IgResults;
    }
    public List<SearchResultItem> getTwitterResults() {
        return TwitterResults;
    }
}
