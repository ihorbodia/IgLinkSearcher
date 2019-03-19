package Commands;

import Servcies.DIResolver;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CheckInstagramSearchActionCommand extends AbstractAction {

    private final DIResolver diResolver;

    public CheckInstagramSearchActionCommand(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
