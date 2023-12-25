package ui.frame;

import javax.swing.*;

public class  MainFrame {
    private static MainFrame mainFrame = null;
    private JFrame frame;

    private MainFrame() {
        frame = new JFrame();
        frame.setTitle("BCS - Budget Control System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(900, 660);
        frame.setLocationRelativeTo(null);
    }

    public static MainFrame getInstance() {
        return mainFrame == null ? mainFrame = new MainFrame() : mainFrame;
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void changePanel(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }
}
