package Servcies;

import Strategies.SearchingMode.SearchModeStrategyBase;
import org.tinylog.Logger;

public class DIResolver {
    private final UserAgentsRotatorService userAgentsRotatorService;
    private final PropertiesService propertiesService;
    private final GuiService guiService;
    private final InputDataService inputDataService;
    private SearchModeStrategyBase currentWorker;

    public DIResolver(UserAgentsRotatorService userAgentsRotatorService,
                      PropertiesService propertiesService,
                      GuiService guiService,
                      InputDataService inputDataService) {

        this.userAgentsRotatorService = userAgentsRotatorService;
        this.propertiesService = propertiesService;
        this.guiService = guiService;
        this.inputDataService = inputDataService;

        Logger.tag("SYSTEM").info("Application started...");
    }

    public UserAgentsRotatorService getUserAgentsRotatorService() {
        return userAgentsRotatorService;
    }

    public PropertiesService getPropertiesService() {
        return propertiesService;
    }

    public GuiService getGuiService() {
        return guiService;
    }

    public InputDataService getInputDataService() {
        return inputDataService;
    }

    public SearchModeStrategyBase getCurrentWorker() {
        return currentWorker;
    }

    public void setCurrentWorker(SearchModeStrategyBase currentWorker) {
        this.currentWorker = currentWorker;
    }
}
