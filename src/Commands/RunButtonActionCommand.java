package Commands;

import Models.Factories.SearchingModeFactory;
import Models.Strategies.SearchingMode.SearchModeStrategyBase;
import Servcies.*;

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
        guiService.setStatusText("Starting...");

        SearchingModeFactory searchingModeFactory = new SearchingModeFactory(diResolver);
        SearchModeStrategyBase searchModeStrategy = searchingModeFactory.createSearchModeStrategy();

        Thread worker = new Thread(() -> {
            guiService.updateStatusText("Starting");
            guiService.changeApplicationStateToWork(true);
            searchModeStrategy.processData(diResolver);
            guiService.changeApplicationStateToWork(false);
            guiService.updateStatusText("Stopped");
        });
        worker.start();
    }
}
