package Tasks;

import Strategies.ParsingStrategies.ParsingStrategyBase;
import org.tinylog.Logger;

import java.util.List;

public class Worker implements Runnable {

    private final RequestTask task;

    public Worker(final RequestTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(task.getTimeoutBeforeStart());
        } catch (InterruptedException e) {
            Logger.error(e);
        }
        List<ParsingStrategyBase> workStrategies = task.getParsingStrategyBases();
        for (ParsingStrategyBase parsingStrategy : workStrategies) {
            parsingStrategy.getSocialMediaResults(task.getCsvItemModel());
        }
    }
}
