package Tasks;

import Servcies.PropertiesService;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import org.tinylog.Logger;

import java.util.List;

public class Worker implements Runnable {

    private final RequestTask task;
    private final PropertiesService propertiesService;

    public Worker(final RequestTask task, final PropertiesService propertiesService) {
        this.task = task;
        this.propertiesService = propertiesService;
    }

    @Override
    public void run() {
        if (propertiesService.getIsWork()) {
            try {
                Thread.sleep(task.getTimeoutBeforeStart());
            } catch (InterruptedException e) {
                Logger.error(e);
            }
            List<ParsingStrategyBase> workStrategies = task.getParsingStrategyBases();
            for (ParsingStrategyBase parsingStrategy : workStrategies) {
                parsingStrategy.getSocialMediaResults(task.getCsvItemModel());
            }
            propertiesService.saveIndex(task.getIndex());
        }
    }
}
