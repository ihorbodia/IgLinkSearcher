package Commands;

import Servcies.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RunButtonActionCommand extends AbstractAction {
    private final DIResolver diResolver;

    public RunButtonActionCommand(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        diResolver.getUserAgentsRotatorService().initList();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();
        guiService.setStatusText("Starting...");

        Thread worker = new Thread(() -> {
            guiService.updateStatusText("Starting");
            guiService.changeApplicationStateToWork(true);
            mainLogicService.StartWork();
            guiService.changeApplicationStateToWork(false);
            guiService.updateStatusText("Stopped");
        });
        worker.start();
    }
}
