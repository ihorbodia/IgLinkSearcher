package Commands;

import Servcies.DIResolver;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StopButtonActionCommand extends AbstractAction {
    private final DIResolver diResolver;

    public StopButtonActionCommand(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
