package Factories;

import Strategies.SearchingMode.SearchModeStrategyBase;
import Strategies.SearchingMode.SearchingWorkerStrategy;
import Servcies.*;
import Utils.DirUtils;
import java.io.File;

public class SearchingModeFactory {
    private final DIResolver diResolver;

    public SearchingModeFactory(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    public SearchModeStrategyBase createSearchModeStrategy() {
        SearchModeStrategyBase searchModeStrategy = null;

        InputDataService inputDataService = diResolver.getInputDataService();
        PropertiesService propertiesService = diResolver.getPropertiesService();

        File inputFile = propertiesService.getSelectedInputFile();
        if (DirUtils.isFileOk(inputFile, "csv")) {
            inputDataService.initInputFile(inputFile);
            inputDataService.initInputFileData();
            searchModeStrategy = new SearchingWorkerStrategy(diResolver);
        }
        return searchModeStrategy;
    }
}
