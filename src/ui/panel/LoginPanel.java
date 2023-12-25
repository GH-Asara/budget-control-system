package ui.panel;

import model.User;
import model.dto.CodeMessageObject;
import model.request.LoginRequest;
import net.miginfocom.swing.MigLayout;
import service.UserService;
import ui.frame.AuthenticationFrame;
import ui.frame.MainFrame;
import util.GlobalVariable;

import javax.swing.*;
import java.awt.*;

public class LoginPanel {
    private static LoginPanel loginPanel = null;
    private AuthenticationFrame authenticationFrame;
    private RegisterPanel registerPanel;
    private MainFrame mainFrame;
    private ActivityPanel activityPanel;
    private BudgetPanel budgetPanel;
    private ClaimPanel claimPanel;
    private UserService userService;
    private JPanel panel;
    private JLabel loginLabel, usernameLabel, passwordLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    private LoginPanel() {
        panel = new JPanel();
        panel.setLayout(new MigLayout("insets 25, fillx"));
        panel.setBackground(Color.WHITE);

        loginLabel = new JLabel();
        loginLabel.setText("Login");
        panel.add(loginLabel, "cell 0 0 2 1, alignx center, gapbottom 15");

        usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        usernameLabel.setLabelFor(usernameTextField);
        panel.add(usernameLabel, "cell 0 1 1 1");

        usernameTextField = new JTextField();
        panel.add(usernameTextField, "cell 1 1 1 1, growx");

        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        passwordLabel.setLabelFor(passwordField);
        panel.add(passwordLabel, "cell 0 2 1 1");

        passwordField = new JPasswordField();
        panel.add(passwordField, "cell 1 2 1 1, growx");

        loginButton = new JButton();
        loginButton.setText("Login");
        loginButton.addActionListener(e -> {
            if (!formValidation()) {
                JOptionPane.showMessageDialog(null, "There Are Still Blank Forms");
            } else {
                login();
            }
        });
        panel.add(loginButton, "cell 0 3 2 1, gaptop 15, growx");

        registerButton = new JButton();
        registerButton.addActionListener(e -> goToRegisterPanel());
        registerButton.setText("Register");

        panel.add(registerButton, "newline, gaptop 20, spanx, growx");
    }

    public static LoginPanel getInstance() {
        return loginPanel == null ? loginPanel = new LoginPanel() : loginPanel;
    }

    public JPanel getPanel() {
        return panel;
    }

    private void clear() {
        usernameTextField.setText("");
        passwordField.setText("");
    }

    private Boolean formValidation() {
        return !usernameTextField.getText().trim().equals("") && !String.valueOf(passwordField.getPassword()).trim().equals("");
    }

    private void goToMainFrame() {
        User user = GlobalVariable.currentUser;
        authenticationFrame = AuthenticationFrame.getInstance();
        mainFrame = MainFrame.getInstance();
        activityPanel = ActivityPanel.getInstance();
        budgetPanel = BudgetPanel.getInstance();
        claimPanel = ClaimPanel.getInstance();
        clear();
        GlobalVariable.currentUser = user;
        activityPanel.setHello(GlobalVariable.currentUser.getName());
        budgetPanel.setHello(GlobalVariable.currentUser.getName());
        claimPanel.setHello(GlobalVariable.currentUser.getName());
        authenticationFrame.hide();
        mainFrame.show();
        mainFrame.changePanel(activityPanel.getPanel());
    }

    private void goToRegisterPanel() {
        authenticationFrame = AuthenticationFrame.getInstance();
        registerPanel = RegisterPanel.getInstance();
        clear();
        authenticationFrame.changePanel(registerPanel.getPanel());
    }

    private void login() {
        userService = UserService.getInstance();
        LoginRequest loginRequest = new LoginRequest(usernameTextField.getText(), String.valueOf(passwordField.getPassword()));
        CodeMessageObject<User> userCodeMessageObject = userService.login(loginRequest);
        if (userCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, userCodeMessageObject.getMessage());
        } else {
            goToMainFrame();
        }
    }
}
