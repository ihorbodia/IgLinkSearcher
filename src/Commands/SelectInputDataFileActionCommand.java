package Commands;

import Servcies.DIResolver;
import Servcies.*;
import Utils.DirUtils;
import org.tinylog.Logger;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class SelectInputDataFileActionCommand extends AbstractAction {
    private final DIResolver diResolver;

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input data file button action performed");
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        InputDataService inputDataService = diResolver.getInputDataService();

        File inputData = guiService.selectFileDialog("Select CSV data file", "csv");
        if (DirUtils.isFileOk(inputData, "csv")) {
            guiService.setInputFilePath(inputData.getAbsolutePath());
            propertiesService.saveSelectedInputFile(inputData.getAbsolutePath());
            inputDataService.initInputFile(inputData);
        }
    }

    public SelectInputDataFileActionCommand(DIResolver diResolver) {
        super("Choose file");
        this.diResolver = diResolver;
    }
}
