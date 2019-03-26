package Servcies;

import GUI.*;
import Utils.StrUtils;
import org.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

public class GuiService {
    private Bootstrapper bootstrapper;

    public GuiService() {
    }

    public void setCheckedInstagramSearch(boolean isInstagramSearch) {
        bootstrapper.getIsIngCheckBox().setSelected(isInstagramSearch);
    }

    public void setCheckedTwitterSearch(boolean isTwitterSearch) {
        bootstrapper.getIsTwitterCheckBox().setSelected(isTwitterSearch);
    }

    public void setCheckedIsOverwriteOldLinks(boolean isOverwriteOldLinks) {
        bootstrapper.getOverwriteOldLinksCB().setSelected(isOverwriteOldLinks);
    }

    public void setInputFilePath(String path) {
        bootstrapper.getSelectedFileLabelData().setText(StrUtils.cutPath(path));
    }

    public void updateStatusText(String statusText) {
        bootstrapper.getLabelStatusData().setText(statusText);
    }

    public void changeApplicationStateToWork(boolean isWorkState) {
        bootstrapper.getRunButton().setEnabled(!isWorkState);
        bootstrapper.getStopButton().setEnabled(isWorkState);
        bootstrapper.getSelectFileButton().setEnabled(!isWorkState);
        bootstrapper.getIsIngCheckBox().setEnabled(!isWorkState);
        bootstrapper.getIsTwitterCheckBox().setEnabled(!isWorkState);
        bootstrapper.getOverwriteOldLinksCB().setEnabled(!isWorkState);
    }

    public void setBootstrapper(Bootstrapper bootstrapper) {
        this.bootstrapper = bootstrapper;
    }

    public File selectFileDialog(String title, String extension) {
        String osName = System.getProperty("os.name");
        File result = null;

        FilenameFilter filter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith("." + extension);
        };

        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(bootstrapper, title);
            chooser.setFilenameFilter(filter);
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            chooser.setVisible(true);
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            if (chooser.getFile() != null) {
                result = new File(chooser.getDirectory() + chooser.getFile());
            }
        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle(title);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = chooser.showDialog(bootstrapper, title);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                result = new File(userSelectedFolder.getAbsolutePath());
            }
        }
        return result;
    }

    public void setStatusText(String text) {
        bootstrapper.getLabelStatusData().setText(text);
    }
}
