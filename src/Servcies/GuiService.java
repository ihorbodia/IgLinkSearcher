package Servcies;

import GUI.*;
import org.tinylog.Logger;

public class GuiService {
    private Bootstrapper bootstrapper;

    public GuiService() {
    }

    public void checkInstagramSearch(boolean isInstagramSearch) {
        bootstrapper.getIsIngCheckBox().setSelected(isInstagramSearch);
    }

    public void checkTwitterSearch(boolean isTwitterSearch) {
        bootstrapper.getIsTwitterCheckBox().setSelected(isTwitterSearch);
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

    public void setIsEnabledRunButton(boolean enabled) {
        bootstrapper.getRunButton().setEnabled(enabled);
    }

    public void setBootstrapper(Bootstrapper bootstrapper) {
        this.bootstrapper = bootstrapper;
    }

    public void setStatusText(String text) {
        bootstrapper.getLabelStatusData().setText(text);
    }

    public void updateCountItemsStatus(int currentItem, int totalItems) {

        if (totalItems > 1) {
            setStatusText("Processed " + currentItem + "/" + (totalItems - 1) +" items.");
        }
        else {
            setStatusText("Processed " + currentItem + "/" + (totalItems) +" items");
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Logger.tag("SYSTEM").error(e, "Interrupt exception");
        }
    }
}
