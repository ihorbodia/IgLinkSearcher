package Models;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class SearchResultItem {
    public String MainHeader;
    public String SearchedLink;
    public String Description;

    public SearchResultItem(Element div) {
        MainHeader = div.select("h3").text();
        if (StringUtils.isEmpty(MainHeader)){
            try {
                MainHeader = div.child(0).child(0).text();
            } catch (Exception ignored) { }
        }

        SearchedLink = div.select("div.r > a").attr("href");
        if (StringUtils.isEmpty(SearchedLink)){
            SearchedLink = div.select("h3.r > a").attr("href");
        }
        if (StringUtils.isEmpty(SearchedLink)){
            try {
                SearchedLink = div.child(0).child(0).select("a").attr("href");
            } catch (Exception ignored) { }
        }
        //SearchedLink =  UrlUtils.clearLink(SearchedLink);
        Description = div.select("div.s").text();
    }
}
