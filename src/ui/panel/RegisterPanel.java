package ui.panel;

import model.User;
import model.dto.CodeMessageObject;
import net.miginfocom.swing.MigLayout;
import service.UserService;
import ui.frame.AuthenticationFrame;
import ui.frame.MainFrame;
import util.GlobalVariable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class RegisterPanel {
    private static RegisterPanel registerPanel = null;
    private AuthenticationFrame authenticationFrame;
    private LoginPanel loginPanel;
    private MainFrame mainFrame;
    private ActivityPanel activityPanel;
    private BudgetPanel budgetPanel;
    private ClaimPanel claimPanel;
    private UserService userService;
    private JPanel panel;
    private JLabel registerLabel, nameLabel, passwordLabel, roleLabel;
    private JTextField nameTextField;
    private JPasswordField passwordField;
    private JComboBox roleComboBox;
    private JButton loginButton, registerButton;

    private RegisterPanel() {
        panel = new JPanel();
        panel.setLayout(new MigLayout("insets 25, fillx"));
        panel.setBackground(Color.WHITE);

        registerLabel = new JLabel();
        registerLabel.setText("Register");
        panel.add(registerLabel, "cell 0 0 2 1, alignx center, gapbottom 15");

        nameLabel = new JLabel();
        nameLabel.setText("Name");
        nameLabel.setLabelFor(nameTextField);
        panel.add(nameLabel, "cell 0 1 1 1");

        nameTextField = new JTextField();
        panel.add(nameTextField, "cell 1 1 1 1, growx");

        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        passwordLabel.setLabelFor(passwordField);
        panel.add(passwordLabel, "cell 0 2 1 1");

        passwordField = new JPasswordField();
        panel.add(passwordField, "cell 1 2 1 1, growx");

        roleLabel = new JLabel();
        roleLabel.setText("Role");
        roleLabel.setLabelFor(passwordField);
        panel.add(roleLabel, "cell 0 3 1 1");

        roleComboBox = new JComboBox<>(GlobalVariable.roles.toArray());
        roleComboBox.setSelectedIndex(-1);
        panel.add(roleComboBox, "cell 1 3 1 1, growx");

        registerButton = new JButton();
        registerButton.setText("Register");
        registerButton.addActionListener(e -> {
            if (!formValidation()) {
                JOptionPane.showMessageDialog(null, "There Are Still Blank Forms");
            } else {
                register();
            }
        });
        panel.add(registerButton, "cell 0 4 2 1, gaptop 15, growx");

        loginButton = new JButton();
        loginButton.addActionListener(e -> goToLoginPanel());
        loginButton.setText("Login");

        panel.add(loginButton, "cell 0 5 2 1, gaptop 20, growx");
    }

    public static RegisterPanel getInstance() {
        return registerPanel == null ? registerPanel = new RegisterPanel() : registerPanel;
    }

    public JPanel getPanel() {
        return panel;
    }

    private void clear() {
        nameTextField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(-1);
    }

    private Boolean formValidation() {
        return !nameTextField.getText().trim().equals("") && !String.valueOf(passwordField.getPassword()).trim().equals("") && !Objects.requireNonNull(roleComboBox.getSelectedItem()).toString().trim().equals("");
    }

    private void goToMainFrame() {
        authenticationFrame = AuthenticationFrame.getInstance();
        mainFrame = MainFrame.getInstance();
        activityPanel = ActivityPanel.getInstance();
        budgetPanel = BudgetPanel.getInstance();
        claimPanel = ClaimPanel.getInstance();
        clear();
        activityPanel.setHello(GlobalVariable.currentUser.getName());
        budgetPanel.setHello(GlobalVariable.currentUser.getName());
        claimPanel.setHello(GlobalVariable.currentUser.getName());
        authenticationFrame.hide();
        mainFrame.show();
        mainFrame.changePanel(claimPanel.getPanel());
    }

    private void goToLoginPanel() {
        authenticationFrame = AuthenticationFrame.getInstance();
        loginPanel = LoginPanel.getInstance();
        clear();
        authenticationFrame.changePanel(loginPanel.getPanel());
    }

    private void register() {
        userService = UserService.getInstance();
        User user = new User(nameTextField.getText(), String.valueOf(passwordField.getPassword()), Objects.requireNonNull(roleComboBox.getSelectedItem()).toString());
        CodeMessageObject<User> userCodeMessageObject = userService.register(user);
        if (userCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, userCodeMessageObject.getMessage());
        } else {
            goToMainFrame();
        }
    }
}
