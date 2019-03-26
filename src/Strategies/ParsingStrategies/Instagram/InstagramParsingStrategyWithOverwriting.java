package Strategies.ParsingStrategies.Instagram;

import Models.CsvItemModel;
import Servcies.DIResolver;

public class InstagramParsingStrategyWithOverwriting extends BaseInstagramParsingStrategy {

    public InstagramParsingStrategyWithOverwriting(DIResolver diResolver) {
        super(diResolver);
    }

    @Override
    public void getSocialMediaResults(CsvItemModel csvItemModel) {
        getIgResults(csvItemModel);
    }
}
