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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SearchingWorkerStrategy extends SearchModeStrategyBase {
    public SearchingWorkerStrategy(DIResolver diResolver) {
        this.guiService = diResolver.getGuiService();
        this.inputDataService = diResolver.getInputDataService();
        this.propertiesService = diResolver.getPropertiesService();
    }

    private final GuiService guiService;
    private final InputDataService inputDataService;
    private final PropertiesService propertiesService;
    private ExecutorService executor;
    private boolean isWork;

    @Override
    public void processData(DIResolver diResolver) throws InterruptedException {
        isWork  = true;
        guiService.updateStatusText("Processing started");
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

        for (int i = 0; i < size; i++) {
            tasks.add(new RequestTask(i, csvFileData.get(i), parsingStrategyBases));
        }
        executor = Executors.newFixedThreadPool(50);

        for (RequestTask task : tasks) {
            Runnable worker = new Worker(task, propertiesService);
            executor.execute(worker);
        }
        while (!executor.isTerminated()) {
            Thread.sleep(1000);
            if (isWork) {
                guiService.updateStatusText(String.format("Processed %d/%d items.", ((ThreadPoolExecutor) executor).getCompletedTaskCount(), ((ThreadPoolExecutor) executor).getTaskCount()));
            }
            inputDataService.updateResultCsvItems();
            if (((ThreadPoolExecutor) executor).getCompletedTaskCount() == size) {
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
