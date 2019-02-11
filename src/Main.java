import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static MainForm gui;
    static PropertiesHelper properties;
    public static IgSearcherLogic logic;

    public static void main(String[] args) {
        initLookAndFeel();
        initProperties();
        initGUI();
        initLogic();
    }

    private static void initGUI() {
        gui = new MainForm();

        gui.setTitle("Instagram link searcher v1.9");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setSize(600, 150);
    }

    private static void initLogic() {
        logic = new IgSearcherLogic(properties);

        String restoredPath = properties.restoreProperty("selectedCsvInputFile");
        if(!StringUtils.isEmpty(restoredPath)) {
            if (Files.exists(Paths.get(restoredPath))) {
                gui.setInputFilePath(restoredPath);
            }
        }

        if(Boolean.valueOf(properties.restoreProperty("isWorked"))) {
            logic.Run();
        }
    }

    private static void initProperties() {
        properties = new PropertiesHelper();
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
