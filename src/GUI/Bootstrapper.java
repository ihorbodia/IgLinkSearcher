package GUI;

import Commands.*;
import Servcies.DIResolver;

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
    private JCheckBox overwriteOldLinksCB;

    public Bootstrapper(DIResolver diResolver) {
        getStopButton().setEnabled(false);
        this.setContentPane(mainPanel);

        mainPanel.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        runButton.setAction(new RunButtonActionCommand(diResolver));
        stopButton.setAction(new StopButtonActionCommand(diResolver));
        selectFileButton.setAction(new SelectInputDataFileActionCommand(diResolver));
        isIngCheckBox.setAction(new CheckInstagramSearchActionCommand(diResolver));
        isTwitterCheckBox.setAction(new CheckTwitterSearchActionCommand(diResolver));
        getOverwriteOldLinksCB().setAction(new CheckOverwriteOldLinksActionCommand(diResolver));
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

    public JCheckBox getOverwriteOldLinksCB() {
        return overwriteOldLinksCB;
    }
}
