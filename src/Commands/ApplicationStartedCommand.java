package Commands;

import Servcies.DIResolver;
import Servcies.GuiService;
import Servcies.PropertiesService;
import Utils.DirUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ApplicationStartedCommand extends AbstractAction {

    private final DIResolver diResolver;

    public ApplicationStartedCommand(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();

        boolean isInstagramSearch = propertiesService.getIsIgSearch();
        guiService.setCheckedInstagramSearch(isInstagramSearch);

        boolean isTwitterSearch = propertiesService.getIsTwitterSearch();
        guiService.setCheckedTwitterSearch(isTwitterSearch);

        File inputFile = propertiesService.getSelectedInputFile();
        if (DirUtils.isFileOk(inputFile, "csv")) {
            guiService.setInputFilePath(inputFile.getAbsolutePath());
        }

        if (propertiesService.getIsWork() && (isInstagramSearch || isTwitterSearch)) {
            Thread worker = new Thread(() -> {
                RunButtonActionCommand runButtonActionCommand = new RunButtonActionCommand(diResolver);
                runButtonActionCommand.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            });
            worker.start();
        }
    }
}
