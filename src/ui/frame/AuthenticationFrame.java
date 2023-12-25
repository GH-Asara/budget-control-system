package ui.frame;

import javax.swing.*;

public class AuthenticationFrame {
    private static AuthenticationFrame authenticationFrame = null;
    private JFrame frame;

    private AuthenticationFrame() {
        frame = new JFrame();
        frame.setTitle("BCS - Authentication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(420, 360);
        frame.setLocationRelativeTo(null);
    }

    public static AuthenticationFrame getInstance() {
        return authenticationFrame == null ? authenticationFrame = new AuthenticationFrame() : authenticationFrame;
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
