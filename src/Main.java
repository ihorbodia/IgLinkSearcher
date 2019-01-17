import javax.swing.*;

public class Main {

    static MainForm gui;
    static IgSearcherLogic logic;

    public static void main(String[] args) {
        initLookAndFeel();
        initLogic();
        initGUI();
    }

    private static void initGUI() {
        gui = new MainForm(logic);

        gui.setTitle("Ig link searcher v1.0");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setSize(600, 150);
    }

    private static void initLogic() {
        logic = new IgSearcherLogic();
    }

    private static void initLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }
}
