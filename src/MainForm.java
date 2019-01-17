import javax.swing.*;

public class MainForm extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;
    private JLabel labelStatus;
    private JLabel labelStatusData;
    private JButton stopButton;
    private JLabel selectedFileLabelData;
    private JLabel selectedFileLabel;

    public MainForm(IgSearcherLogic logicObj) {
        stopButton.setEnabled(false);
        this.setContentPane(mainPanel);

        logicObj.restoreProperties();
        runButton.addActionListener(e -> logicObj.Run());
        stopButton.addActionListener(e -> logicObj.Stop());
        mainPanel.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}