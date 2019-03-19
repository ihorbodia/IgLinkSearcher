package Utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

public class DirUtils {
    public static File selectFileDialog(Frame frame, String title, String extension) {
        String osName = System.getProperty("os.name");
        File result = null;

        FilenameFilter filter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith("." + extension);
        };

        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(frame, title);
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

            int returnVal = chooser.showDialog(frame, title);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                result = new File(userSelectedFolder.getAbsolutePath());
            }
        }
        return result;
    }

    public static File selectFolderDialog(Frame frame, String title) {
        String osName = System.getProperty("os.name");
        File result = null;
        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(frame, title);
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            chooser.setVisible(true);
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            if (chooser.getFile() != null) {
                result = new File(chooser.getDirectory());
            }
        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select file");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnVal = chooser.showDialog(frame, title);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                result = new File(userSelectedFolder.getAbsolutePath());
            }
        }
        return result;
    }

    public static boolean isFileOk(File file, String extension) {
        if (file != null && !StringUtils.isEmpty(file.getAbsolutePath()) && file.exists() && file.isFile() && FilenameUtils.isExtension(file.getAbsolutePath(), extension)) {
            return true;
        }
        return false;
    }

    public static boolean isDirOk(File dir) {
        if (dir == null || StringUtils.isEmpty(dir.getAbsolutePath()) || !dir.exists() || !dir.isDirectory()) {
            return false;
        }
        return true;
    }
}
