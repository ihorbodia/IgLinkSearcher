package Helpers;

import GUI.Bootstrapper;
import Logic.MainLogicService;
import Utils.DialogUtils;

import java.io.File;
import java.nio.file.Files;

public class ServicesHandler {

    private MainLogicService mainLogicService;
    private PropertiesHelper properties;
    private GuiHelper gui;

    private Bootstrapper bootstrapper;

    public ServicesHandler() {
        initProperties();
        initGUI();
        initLogic();
        mapGuiActionsToLogic();
    }

    private void initGUI() {
        bootstrapper = new Bootstrapper();
        bootstrapper.setTitle("Instagram finder v2.0");
        bootstrapper.setVisible(true);
        bootstrapper.setResizable(false);
        bootstrapper.setSize(500, 130);
        gui = new GuiHelper(bootstrapper);
    }

    private void initLogic() {
        mainLogicService = new MainLogicService(properties, gui);
        mainLogicService.ApplicationStart();
    }

    private void mapGuiActionsToLogic() {
        bootstrapper.getRunButton().addActionListener(e -> mainLogicService.Run());
        bootstrapper.getStopButton().addActionListener(e -> mainLogicService.Stop());
        bootstrapper.getSelectFileButton().addActionListener(e -> {
            File file = FilesHelper.setUpInputFile(DialogUtils.selectFolderDialog());
            mainLogicService.setInputFilePath(file);
        });
        bootstrapper.getIsIngCheckBox().addActionListener(e -> mainLogicService.setIsIgSearch(bootstrapper.getIsIngCheckBox().isSelected()));
        bootstrapper.getIsTwitterCheckBox().addActionListener(e -> mainLogicService.setIsTwitterSearch(bootstrapper.getIsTwitterCheckBox().isSelected()));
    }

    private void initProperties() {
        properties = new PropertiesHelper();
    }
}
