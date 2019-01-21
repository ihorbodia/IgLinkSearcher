import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainForm extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;
    private JLabel labelStatus;
    private JLabel labelStatusData;
    private JButton stopButton;
    private JLabel selectedFileLabelData;
    private JLabel selectedFileLabel;
    private JButton selectFileButton;

    public MainForm(IgSearcherLogic logicObj) {
        stopButton.setEnabled(false);
        this.setContentPane(mainPanel);

        logicObj.restoreProperties();
        runButton.addActionListener(e -> logicObj.Run(labelStatusData, runButton, stopButton));
        stopButton.addActionListener(e -> logicObj.Stop());
        selectFileButton.addActionListener(e -> {
                    String inputFilePath = selectFolderDialog();
                    logicObj.setInputFilePath(inputFilePath);
                    selectedFileLabelData.setText(inputFilePath);
                }
        );

        mainPanel.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private String selectFolderDialog() {
        String osName = System.getProperty("os.name");
        String result = "";
        if (osName.equalsIgnoreCase("mac os x")) {
            FileDialog chooser = new FileDialog(MainForm.this, "Select file");
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            chooser.setVisible(true);

            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            if (chooser.getDirectory() != null) {
                String folderName = chooser.getDirectory();
                folderName += chooser.getFile();
                result = folderName;
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
                return selectedFileLabelData.getText();
            }
        }
        return cutPath(result);
    }

    private String cutPath(String path) {
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
}