package Strategies.SearchingMode;

import Servcies.DIResolver;

public abstract class SearchModeStrategyBase {
    public abstract void processData(DIResolver diResolver);
    public abstract void stopProcessing();
}
