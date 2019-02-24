package Models;

import Utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class SearchResultItem {
    public String MainHeader;
    public String SearchedLink;
    public String Description;

    SearchResultItem(Element div) {
        MainHeader = div.select("h3").text();
        SearchedLink = div.select("div.r > a").attr("href");
        if (StringUtils.isEmpty(SearchedLink)){
            SearchedLink = div.select("h3.r > a").attr("href");
        }
        SearchedLink = UrlUtils.clearLink(SearchedLink);
        Description = div.select("div.s").text();
    }
}
