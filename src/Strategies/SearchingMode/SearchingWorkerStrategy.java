package Strategies.SearchingMode;

import Exceptions.InputFileEmptyException;
import Models.CsvItemModel;
import Servcies.PropertiesService;
import Strategies.ParsingStrategies.InstagramParsingStrategy;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Strategies.ParsingStrategies.TwitterParsingStrategy;
import Servcies.DIResolver;
import Servcies.GuiService;
import Servcies.InputDataService;
import Tasks.RequestTask;
import Tasks.Worker;
import javafx.concurrent.Task;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService executor;

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
        List<RequestTask> tasks = new ArrayList<>();

        for (int i = index; i < size; i++) {
            tasks.add(new RequestTask(csvFileData.get(i), parsingStrategyBases));
        }
        //guiService.updateStatusText(String.format("Processed %d/%d items.", i, size));

        executor = Executors.newFixedThreadPool(5);

        for (RequestTask task : tasks) {
            Runnable worker = new Worker(task);
            executor.execute(worker);
        }
        // All tasks were executed, now shutdown
        //executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Program finished");

        inputDataService.updateResultCsvItems();
        //propertiesService.saveIndex(i);
    }

    @Override
    public void stopProcessing() {
        isWork = false;
        executor.shutdown();
    }
}
