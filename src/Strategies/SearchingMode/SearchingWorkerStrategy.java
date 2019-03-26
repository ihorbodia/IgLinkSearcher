package Strategies.SearchingMode;

import Exceptions.InputFileEmptyException;
import Models.CsvItemModel;
import Servcies.PropertiesService;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Servcies.DIResolver;
import Servcies.GuiService;
import Servcies.InputDataService;
import Tasks.RequestTask;
import Tasks.Worker;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SearchingWorkerStrategy extends SearchModeStrategyBase {
    public SearchingWorkerStrategy(DIResolver diResolver, List<ParsingStrategyBase> parsingStrategyBases) {
        this.guiService = diResolver.getGuiService();
        this.inputDataService = diResolver.getInputDataService();
        this.propertiesService = diResolver.getPropertiesService();
        this.parsingStrategyBases = parsingStrategyBases;
    }
    private final List<ParsingStrategyBase> parsingStrategyBases;
    private final GuiService guiService;
    private final InputDataService inputDataService;
    private final PropertiesService propertiesService;
    private ThreadPoolExecutor executor;
    private boolean isWork;

    @Override
    public void processData(DIResolver diResolver) {
        isWork  = true;
        guiService.updateStatusText("Processing started");
        List<CsvItemModel> csvFileData = diResolver.getInputDataService().getInputCsvModelItems();
        int size = diResolver.getInputDataService().getInputCsvModelItems().size();

        if (size == 0) {
            throw new InputFileEmptyException("Input data file doesn't contain elements");
        }
        List<RequestTask> tasks = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            tasks.add(new RequestTask(i, csvFileData.get(i), parsingStrategyBases));
        }
        executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(50);

        for (RequestTask task : tasks) {
            Runnable worker = new Worker(task, propertiesService);
            executor.execute(worker);
        }
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.error(e);
            }
            if (isWork) {
                guiService.updateStatusText(String.format("Processed %d/%d items.", executor.getCompletedTaskCount(), executor.getTaskCount()));
            }
            inputDataService.updateResultCsvItems();
            if (executor.getCompletedTaskCount() == size) {
                stopProcessing();
            }
        }
    }

    @Override
    public void stopProcessing() {
        executor.shutdown();
        isWork = false;
        propertiesService.saveIsWork(isWork);
    }
}
