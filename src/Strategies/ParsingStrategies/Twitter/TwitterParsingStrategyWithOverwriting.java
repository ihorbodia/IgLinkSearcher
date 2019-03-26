package Strategies.ParsingStrategies.Twitter;

import Models.CsvItemModel;
import Servcies.DIResolver;

public class TwitterParsingStrategyWithOverwriting extends BaseTwitterParsingStrategy {

    public TwitterParsingStrategyWithOverwriting(DIResolver diResolver) {
        super(diResolver);
    }

    @Override
    public void getSocialMediaResults(CsvItemModel csvItemModel) {
        getTwitterResults(csvItemModel);
    }
}
