import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainForm extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;
    private JLabel labelStatus;
    private JLabel labelStatusData;
    private JButton stopButton;
    private JLabel selectedFileLabelData;
    private JLabel selectedFileLabel;
    private JButton selectFileButton;

    public MainForm() {
        getStopButton().setEnabled(false);
        this.setContentPane(mainPanel);

        getRunButton().addActionListener(e -> Main.logic.Run());
        getStopButton().addActionListener(e -> Main.logic.Stop());
        getSelectFileButton().addActionListener(e -> {
                    setInputFilePath(selectFolderDialog());
                }
        );

        mainPanel.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setInputFilePath(String inputFilePath) {
        File f = new File(inputFilePath);
        if (f.exists()) {
            Main.logic.setInputFilePath(inputFilePath);
            selectedFileLabelData.setText(cutPath(inputFilePath));
        } else
        {
            getLabelStatusData().setText("Something wrong with input file");
        }
    }

    private String selectFolderDialog() {
        String osName = System.getProperty("os.name");
        String result = "";
        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(MainForm.this, "Select file");
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            chooser.setVisible(true);

            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            if (chooser.getFile() != null) {
                String fileName = chooser.getFile();
                result = fileName;
            }
        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select target file");
            chooser.setFileSelectionMode(chooser.FILES_ONLY);

            int returnVal = chooser.showDialog(MainForm.this, "Select file");
            String folderName = "";
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userSelectedFolder = chooser.getSelectedFile();
                folderName = userSelectedFolder.getAbsolutePath();
                result = folderName;
            }
            if (StringUtils.isEmpty(folderName)){
                return getSelectedFileLabelData().getText();
            }
        }
        return result;
    }

    public String cutPath(String path) {
        int size = 70;
        if (path.length() <= size) {
            return path;
        } else if (path.length() > size) {
            return "..."+path.substring(path.length() - (size - 3));
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("Something wrong with file path cut");
        }
    }

    public JLabel getLabelStatusData() {
        return labelStatusData;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JLabel getSelectedFileLabelData() {
        return selectedFileLabelData;
    }

    public JButton getSelectFileButton() {
        return selectFileButton;
    }
}