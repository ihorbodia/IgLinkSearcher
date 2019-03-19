package Models.Strategies.ParsingStrategies;

import Models.CsvItemModel;
import org.jsoup.nodes.Element;

public interface IParsingStrategy {

    Element getSocialMediaDataFromResults(CsvItemModel item);

}
