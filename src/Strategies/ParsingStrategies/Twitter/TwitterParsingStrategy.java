package Strategies.ParsingStrategies.Twitter;

import Models.CsvItemModel;
import Servcies.DIResolver;
import org.apache.commons.lang3.StringUtils;

public class TwitterParsingStrategy extends BaseTwitterParsingStrategy {

    public TwitterParsingStrategy(DIResolver diResolver) {
        super(diResolver);
    }

    @Override
    public void getSocialMediaResults(CsvItemModel csvItemModel) {
        if (StringUtils.isEmpty(csvItemModel.foundInstagram)) {
            getTwitterResults(csvItemModel);
        }
    }
}
