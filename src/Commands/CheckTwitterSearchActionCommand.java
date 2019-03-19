package Commands;

import Servcies.DIResolver;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CheckTwitterSearchActionCommand extends AbstractAction {
    private final DIResolver diResolver;

    public CheckTwitterSearchActionCommand(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
