package Tasks;

import Models.CsvItemModel;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Utils.RandomUtils;

import java.util.List;

public class RequestTask {

    private CsvItemModel csvItemModel;
   private final int timeoutBeforeStart;
   private final List<ParsingStrategyBase> parsingStrategyBases;

    public RequestTask(CsvItemModel csvItemModel, List<ParsingStrategyBase> parsingStrategyBases) {
        this.timeoutBeforeStart = RandomUtils.getRandomMilliseconds(1000, 100000);
        this.parsingStrategyBases = parsingStrategyBases;
        this.csvItemModel = csvItemModel;
    }

    public int getTimeoutBeforeStart() {
        return timeoutBeforeStart;
    }

    public List<ParsingStrategyBase> getParsingStrategyBases() {
        return parsingStrategyBases;
    }

    public CsvItemModel getCsvItemModel() {
        return csvItemModel;
    }
}
