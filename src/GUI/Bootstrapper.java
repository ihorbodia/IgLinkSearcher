package GUI;

import javax.swing.*;

public class Bootstrapper extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;
    private JLabel labelStatus;
    private JLabel labelStatusData;
    private JButton stopButton;
    private JLabel selectedFileLabelData;
    private JLabel selectedFileLabel;
    private JButton selectFileButton;
    private JCheckBox isIngCheckBox;
    private JCheckBox isTwitterCheckBox;

    public Bootstrapper() {
        getStopButton().setEnabled(false);
        this.setContentPane(mainPanel);

        mainPanel.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public String cutPath(String path) {
        int size = 70;
        if (path.length() <= size) {
            return path;
        } else if (path.length() > size) {
            return "..." + path.substring(path.length() - (size - 3));
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

    public JCheckBox getIsIngCheckBox() {
        return isIngCheckBox;
    }

    public JCheckBox getIsTwitterCheckBox() {
        return isTwitterCheckBox;
    }
}
