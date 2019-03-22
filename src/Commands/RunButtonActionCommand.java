package Commands;

import Factories.SearchingModeFactory;
import Strategies.SearchingMode.SearchModeStrategyBase;
import Servcies.*;
import org.tinylog.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RunButtonActionCommand extends AbstractAction {
    private final DIResolver diResolver;

    public RunButtonActionCommand(DIResolver diResolver) {
        super("Run");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        diResolver.getUserAgentsRotatorService().initList();
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        guiService.setStatusText("Starting...");

        SearchingModeFactory searchingModeFactory = new SearchingModeFactory(diResolver);
        SearchModeStrategyBase searchModeStrategy = searchingModeFactory.createSearchModeStrategy();

        try {
        Thread worker = new Thread(() -> {
            diResolver.setCurrentWorker(searchModeStrategy);
            guiService.updateStatusText("Starting");
            guiService.changeApplicationStateToWork(true);
            propertiesService.saveIsWork(true);
            searchModeStrategy.processData(diResolver);
            guiService.changeApplicationStateToWork(false);
            guiService.updateStatusText("Stopped");
        });
        worker.start();
        } catch (Exception ex) {
            Logger.error(ex, "Application aborted");
            guiService.setStatusText("Application aborted");
        }
    }
}
