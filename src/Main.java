import Commands.ApplicationStartedCommand;
import GUI.Bootstrapper;
import Servcies.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class Main {

    public static void main(String[] args) {
        initLookAndFeel();
        Main main = new Main();
        main.start();
    }

    public void start() {
        initLookAndFeel();

        GuiService guiService = new GuiService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();
        PropertiesService propertiesService = new PropertiesService();
        InputDataService inputDataService = new InputDataService();

        DIResolver diResolver = new DIResolver(userAgentsRotatorService, propertiesService, guiService, inputDataService);

        Bootstrapper bootstrapper = new Bootstrapper(diResolver);
        bootstrapper.setTitle("Social finder v2.6");
        bootstrapper.setVisible(true);
        bootstrapper.setResizable(false);
        bootstrapper.setSize(500, 130);

        guiService.setBootstrapper(bootstrapper);

        ApplicationStartedCommand applicationStartedActionCommand = new ApplicationStartedCommand(diResolver);
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
