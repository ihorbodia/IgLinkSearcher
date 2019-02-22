package Helpers;

import GUI.Bootstrapper;

public class GuiHelper {

    public static Bootstrapper bootstrapper;

    public GuiHelper(Bootstrapper bootstrapper) {
        this.bootstrapper = bootstrapper;
    }

    public void checkInstagramSearch(boolean isInstagramSearch) {
        bootstrapper.getIsIngCheckBox().setSelected(isInstagramSearch);
    }

    public void checkTwitterSearch(boolean isTwitterSearch) {
        bootstrapper.getIsIngCheckBox().setSelected(isTwitterSearch);
    }

    public void setInputFilePath(String path) {
        bootstrapper.getSelectedFileLabelData().setText(path);
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
    }
}
