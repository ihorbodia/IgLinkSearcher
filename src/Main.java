import Commands.ApplicationStartedCommand;
import GUI.Bootstrapper;
import Handlers.ServicesHandler;
import Servcies.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Main {

    public static void main(String[] args) {
        initLookAndFeel();
        Main main = new Main();
        main.start();
        new ServicesHandler();
    }

    public void start() {
        initLookAndFeel();

        GuiService guiService = new GuiService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();
        PropertiesService propertiesService = new PropertiesService();
        OutputDataService outputDataService = new OutputDataService();
        InputDataService inputDataService = new InputDataService();

        DIResolver diResolver = new DIResolver(userAgentsRotatorService, propertiesService, guiService, outputDataService, inputDataService);

        Bootstrapper bootstrapper = new Bootstrapper(diResolver);
        bootstrapper.setTitle("Instagram finder v2.2");
        bootstrapper.setVisible(true);
        bootstrapper.setResizable(false);
        bootstrapper.setSize(500, 130);

        guiService.setBootstrapper(bootstrapper);

        ApplicationStartedCommand applicationStartedActionCommand = new ApplicationStartedCommand();
        applicationStartedActionCommand.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }

    private static void initLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }
}
