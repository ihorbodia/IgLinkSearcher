package Commands;

import Servcies.DIResolver;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ApplicationStartedCommand extends AbstractAction {

    private final DIResolver diResolver;

    public ApplicationStartedCommand(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
