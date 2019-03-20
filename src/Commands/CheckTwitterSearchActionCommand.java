package Commands;

import Servcies.DIResolver;
import Servcies.PropertiesService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CheckTwitterSearchActionCommand extends AbstractAction {
    private final DIResolver diResolver;

    public CheckTwitterSearchActionCommand(DIResolver diResolver) {
        super("Twitter");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox cb = (JCheckBox)e.getSource();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        propertiesService.saveIsTwitterSearch(cb.isSelected());
    }
}
