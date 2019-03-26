package Tasks;

import Models.CsvItemModel;
import Strategies.ParsingStrategies.ParsingStrategyBase;
import Utils.RandomUtils;
import java.util.List;

public class RequestTask {

    private CsvItemModel csvItemModel;
    private final int index;
    private final int timeoutBeforeStart;
    private final List<ParsingStrategyBase> parsingStrategyBases;

    public RequestTask(int index, CsvItemModel csvItemModel, List<ParsingStrategyBase> parsingStrategyBases) {
        this.timeoutBeforeStart = RandomUtils.getRandomMilliseconds(1000, 15000);
        this.parsingStrategyBases = parsingStrategyBases;
        this.csvItemModel = csvItemModel;
        this.index = index;
    }

    int getTimeoutBeforeStart() {
        return timeoutBeforeStart;
    }

    List<ParsingStrategyBase> getParsingStrategyBases() {
        return parsingStrategyBases;
    }

    CsvItemModel getCsvItemModel() {
        return csvItemModel;
    }

    public int getIndex() {
        return index;
    }
}
