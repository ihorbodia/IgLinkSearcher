package Commands;

import Servcies.DIResolver;
import Servcies.PropertiesService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CheckInstagramSearchActionCommand extends AbstractAction {

    private final DIResolver diResolver;

    public CheckInstagramSearchActionCommand(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox cb = (JCheckBox)e.getSource();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        propertiesService.saveIsIgSearch(cb.isSelected());
    }
}
