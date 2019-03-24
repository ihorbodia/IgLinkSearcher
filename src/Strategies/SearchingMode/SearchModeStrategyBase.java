package Strategies.SearchingMode;

import Servcies.DIResolver;

public abstract class SearchModeStrategyBase {
    public abstract void processData(DIResolver diResolver) throws InterruptedException;
    public abstract void stopProcessing() throws InterruptedException;
}
