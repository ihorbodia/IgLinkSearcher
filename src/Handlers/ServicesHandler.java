package Handlers;

import GUI.Bootstrapper;
import Helpers.*;
import Logic.MainLogicService;
import Utils.DialogUtils;
import Utils.StrUtils;

import java.io.File;

public class ServicesHandler {

    private MainLogicService mainLogicService;
    private PropertiesHelper propertiesHelper;
    private GuiHelper guiHelper;
    private ProxyHelper proxyHelper;
    private UserAgentRotatorHelper userAgentRotatorHelper;

    private Bootstrapper bootstrapper;

    public ServicesHandler() {
        initProperties();
        initUserAgentsRotator();
        initGUI();
        initProxyService();
        initLogic();
        mapGuiActionsToLogic();
    }

    private void initGUI() {
        bootstrapper = new Bootstrapper();
        bootstrapper.setTitle("Instagram finder v2.1");
        bootstrapper.setVisible(true);
        bootstrapper.setResizable(false);
        bootstrapper.setSize(500, 130);
        guiHelper = new GuiHelper(bootstrapper);
    }

    private void initLogic() {
        mainLogicService = new MainLogicService(propertiesHelper, guiHelper, proxyHelper, userAgentRotatorHelper);
        mainLogicService.ApplicationStart();
    }

    private void initProxyService() {
        proxyHelper = new ProxyHelper(guiHelper);
    }

    private void initUserAgentsRotator() {
        userAgentRotatorHelper = new UserAgentRotatorHelper();
    }

    private void mapGuiActionsToLogic() {
        bootstrapper.getRunButton().addActionListener(e -> {
            Thread worker = new Thread(() -> {
                guiHelper.updateStatusText("Starting");
                guiHelper.changeApplicationStateToWork(true);
                mainLogicService.StartWork();
                guiHelper.changeApplicationStateToWork(false);
                guiHelper.updateStatusText("Stopped");
            });
            worker.start();
        });
        bootstrapper.getStopButton().addActionListener(e -> {
            propertiesHelper.saveIndex(0);
            propertiesHelper.saveIsWork(false);
            guiHelper.updateStatusText("Stopping...");
            mainLogicService.StopWork();
        });
        bootstrapper.getSelectFileButton().addActionListener(e -> {
            File file = FilesHelper.setUpInputFile(DialogUtils.selectFolderDialog());
            mainLogicService.setInputFilePath(file);
            if (file != null) {
                propertiesHelper.saveSelectedInputFile(file.getAbsolutePath());
                guiHelper.setInputFilePath(StrUtils.cutPath(file.getAbsolutePath()));
            }
        });
        bootstrapper.getIsIngCheckBox().addActionListener(e -> mainLogicService.setIsIgSearch(bootstrapper.getIsIngCheckBox().isSelected()));
        bootstrapper.getIsTwitterCheckBox().addActionListener(e -> mainLogicService.setIsTwitterSearch(bootstrapper.getIsTwitterCheckBox().isSelected()));
    }

    private void initProperties() {
        propertiesHelper = new PropertiesHelper();
    }
}
