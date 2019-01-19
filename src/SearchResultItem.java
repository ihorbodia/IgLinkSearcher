import org.jsoup.nodes.Element;

public class SearchResultItem {
    String MainHeader;
    String SearchedLink;
    String Description;
    String MissingKeywords;

    public SearchResultItem(Element div) {
        MainHeader = div.select("h3.r").text();
        SearchedLink = div.select("div.hJND5c").text();
        Description = div.select("div.s").text();
        //MissingKeywords = div.select("div.TXwUJf").text();
    }
}
