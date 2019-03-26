package Commands;

import Servcies.DIResolver;
import Servcies.PropertiesService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CheckOverwriteOldLinksActionCommand extends AbstractAction {
    private final DIResolver diResolver;

    public CheckOverwriteOldLinksActionCommand(DIResolver diResolver) {
        super("Overwrite old links");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox cb = (JCheckBox)e.getSource();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        propertiesService.saveIsOverwriteOldLinks(cb.isSelected());
    }
}
