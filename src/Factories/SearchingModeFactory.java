package Factories;

import Strategies.ParsingStrategies.Instagram.InstagramParsingStrategy;
import Strategies.ParsingStrategies.Instagram.InstagramParsingStrategyWithOverwriting;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Strategies.ParsingStrategies.Twitter.TwitterParsingStrategy;
import Strategies.ParsingStrategies.Twitter.TwitterParsingStrategyWithOverwriting;
import Strategies.SearchingMode.SearchModeStrategyBase;
import Strategies.SearchingMode.SearchingWorkerStrategy;
import Servcies.*;
import Utils.DirUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchingModeFactory {
    private final DIResolver diResolver;

    public SearchingModeFactory(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    public SearchModeStrategyBase createSearchModeStrategy() {
        SearchModeStrategyBase searchModeStrategy = null;

        List<ParsingStrategyBase> parsingStrategyBases = new ArrayList<>();

        InputDataService inputDataService = diResolver.getInputDataService();
        PropertiesService propertiesService = diResolver.getPropertiesService();

        File inputFile = propertiesService.getSelectedInputFile();
        if (DirUtils.isFileOk(inputFile, "csv")) {
            inputDataService.initInputFile(inputFile);
            inputDataService.initInputFileData();
            if (propertiesService.getIsOverwriteOldLinks()) {
                if (propertiesService.getIsIgSearch()) { parsingStrategyBases.add(new InstagramParsingStrategyWithOverwriting(diResolver)); }
                if (propertiesService.getIsTwitterSearch()) { parsingStrategyBases.add(new TwitterParsingStrategyWithOverwriting(diResolver)); }
                searchModeStrategy = new SearchingWorkerStrategy(diResolver, parsingStrategyBases);
            } else {
                if (propertiesService.getIsIgSearch()) { parsingStrategyBases.add(new InstagramParsingStrategy(diResolver)); }
                if (propertiesService.getIsTwitterSearch()) { parsingStrategyBases.add(new TwitterParsingStrategy(diResolver)); }
                searchModeStrategy = new SearchingWorkerStrategy(diResolver, parsingStrategyBases);
            }
        }
        return searchModeStrategy;
    }
}
