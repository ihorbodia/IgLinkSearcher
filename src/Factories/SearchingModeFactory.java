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

        GuiService guiService = diResolver.getGuiService();
        InputDataService inputDataService = diResolver.getInputDataService();
        PropertiesService propertiesService = diResolver.getPropertiesService();

        File inputFile = propertiesService.getSelectedInputFile();
        if (DirUtils.isFileOk(inputFile, "csv")) {
            guiService.setInputFilePath(inputFile.getAbsolutePath());
            inputDataService.initInputFile(inputFile);
        }

        if (diResolver.getPropertiesService().getIsWork() && DirUtils.isFileOk(inputFile, "csv")) {
            searchModeStrategy = new SearchingWorkerStrategy(diResolver);
            inputDataService.initInputFileData();
        }
        return searchModeStrategy;
    }
}
