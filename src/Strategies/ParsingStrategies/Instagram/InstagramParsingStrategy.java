package Strategies.ParsingStrategies.Instagram;

import Models.CsvItemModel;
import Servcies.DIResolver;
import org.apache.commons.lang3.StringUtils;

public class InstagramParsingStrategy extends BaseInstagramParsingStrategy {

    public InstagramParsingStrategy(DIResolver diResolver) {
        super(diResolver);
    }

    @Override
    public void getSocialMediaResults(CsvItemModel csvItemModel) {
        if (StringUtils.isEmpty(csvItemModel.foundInstagram)) {
            getIgResults(csvItemModel);
        }
    }
}
