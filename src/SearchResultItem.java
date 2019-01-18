import org.jsoup.nodes.Element;

public class SearchResultItem {
    String MainHeader;
    String SearchedLink;
    String Description;
    String MissingKeywords;

    public SearchResultItem(Element div) {
        MainHeader = div.select("h3.LC20lb").text();
        SearchedLink = div.select("div.TbwUpd").text();
        Description = div.select("span.st").text();
        MissingKeywords = div.select("div.TXwUJf").text();
    }
}
