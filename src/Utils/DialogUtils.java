package Utils;

import GUI.Bootstrapper;
import Helpers.GuiHelper;
import Helpers.ServicesHandler;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DialogUtils {

    public static String selectFolderDialog() {

        String osName = System.getProperty("os.name");
        String result = "";
        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(GuiHelper.bootstrapper, "Select file");
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            chooser.setVisible(true);
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            if (chooser.getFile() != null) {
                result = chooser.getDirectory() + chooser.getFile();
            }
        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select target file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = chooser.showDialog(GuiHelper.bootstrapper, "Select file");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                result = userSelectedFolder.getAbsolutePath();
            }
        }
        return result.trim();
    }
}
