import org.jsoup.nodes.Element;

public class SearchResultItem {
    String MainHeader;
    String SearchedLink;
    String Description;
    String MissingKeywords;
    public Boolean CorrectItem = false;

    public SearchResultItem(Element div) {
        MainHeader = div.select("a").first().select("h3").text();
        SearchedLink = div.select("a").first().attr("href");
        Description = div.select("div.s").text();
        //MissingKeywords = div.select("div.TXwUJf").text();
    }
}
