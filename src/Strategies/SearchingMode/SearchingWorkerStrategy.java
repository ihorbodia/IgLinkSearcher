package Strategies.SearchingMode;

import Exceptions.InputFileEmptyException;
import Models.CsvItemModel;
import Models.SearchResultItem;
import Servcies.PropertiesService;
import Specifications.Abstract.AbstractSpecification;
import Specifications.Abstract.Specification;
import Specifications.ContainingBusinessDataSpecification;
import Specifications.IgLinksRegexSpecification;
import Specifications.TwitterLinksRegexSpecification;
import Strategies.ParsingStrategies.InstagramParsingStrategy;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Strategies.ParsingStrategies.TwitterParsingStrategy;
import Servcies.DIResolver;
import Servcies.GuiService;
import Servcies.InputDataService;

import java.util.ArrayList;
import java.util.List;

public class SearchingWorkerStrategy extends SearchModeStrategyBase {
    public SearchingWorkerStrategy(DIResolver diResolver) {
        this.guiService = diResolver.getGuiService();
        this.inputDataService = diResolver.getInputDataService();
        this.propertiesService = diResolver.getPropertiesService();
    }

    private final GuiService guiService;
    private final InputDataService inputDataService;
    private final PropertiesService propertiesService;
    private boolean isWork = true;

    @Override
    public void processData(DIResolver diResolver) {
        guiService.updateStatusText("Processing started");
        int index = diResolver.getPropertiesService().getIndex();
        List<CsvItemModel> csvFileData = diResolver.getInputDataService().getInputCsvModelItems();
        int size = diResolver.getInputDataService().getInputCsvModelItems().size();

        if (size == 0) {
            throw new InputFileEmptyException("Input data file doesn't contain elements");
        }

        List<ParsingStrategyBase> parsingStrategyBases = new ArrayList<>();
        if (propertiesService.getIsIgSearch()) {
            parsingStrategyBases.add(new InstagramParsingStrategy(diResolver));
        }

        if (propertiesService.getIsTwitterSearch()) {
            parsingStrategyBases.add(new TwitterParsingStrategy(diResolver));
        }

        for (int i = index; i < size;  i++) {
            if (!isWork) {
                break;
            }
            guiService.updateStatusText(String.format("Processed %d/%d items.", i, size));
            for (ParsingStrategyBase parsingStrategy : parsingStrategyBases) {
                parsingStrategy.getSocialMediaResults(csvFileData.get(i));
            }
            inputDataService.updateResultCsvItems();
            propertiesService.saveIndex(i);
        }
    }

    @Override
    public void stopProcessing() {
        isWork = false;
    }
}
