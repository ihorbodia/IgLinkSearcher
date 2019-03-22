package Commands;

import Servcies.DIResolver;
import Servcies.GuiService;
import Servcies.PropertiesService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StopButtonActionCommand extends AbstractAction {
    private final DIResolver diResolver;

    public StopButtonActionCommand(DIResolver diResolver) {
        super("Stop");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();

        propertiesService.saveIndex(0);
        guiService.updateStatusText("Stopping...");
        propertiesService.saveIsWork(false);
        diResolver.getCurrentWorker().stopProcessing();
    }
}
