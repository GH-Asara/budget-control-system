package ui.panel;

import model.Claim;
import model.User;
import model.dto.CodeMessageObject;
import net.miginfocom.swing.MigLayout;
import repository.UserRepository;
import service.ActivityService;
import service.BudgetService;
import service.ClaimService;
import service.UserService;
import ui.frame.AuthenticationFrame;
import ui.frame.MainFrame;
import util.GlobalVariable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ClaimPanel {
    private static ClaimPanel claimPanel;
    private MainFrame mainFrame;
    private BudgetPanel budgetPanel;
    private ActivityPanel activityPanel;
    private AuthenticationFrame authenticationFrame;
    private LoginPanel loginPanel;
    private ClaimService claimService;
    private UserService userService;
    private ActivityService activityService;
    private BudgetService budgetService;
    private JPanel panel;
    private JButton logoutButton, activityButton, budgetButton, claimButton, approveButton, rejectButton, clearButton, saveButton, deleteButton;
    private JTable claimTable;
    private DefaultTableModel claimTableModel;
    private JLabel helloLabel, remarkLabel, activityLabel, budgetLabel, amountLabel;
    private JTextField remarkTextField;
    private JTextField amountTextField;
    private JComboBox<String> budgetComboBox;
    private JComboBox<String> activityComboBox;
    private Claim currentClaim = null;
    private User user;

    private ClaimPanel() {
        panel = new JPanel();
        panel.setLayout(new MigLayout("insets 25, fillx"));
        panel.setBackground(Color.WHITE);

        helloLabel = new JLabel();
        helloLabel.setText("Hello, ");
        panel.add(helloLabel, "cell 0 0 2 1, alignx left");

        logoutButton = new JButton();
        logoutButton.setText("Logout");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton, "cell 2 0 1 1, w 100, alignx right");

        activityButton = new JButton();
        activityButton.setText("Activity");
        activityButton.addActionListener(e -> goToActivityPanel());
        panel.add(activityButton, "cell 0 1 3 1, w 100, alignx center");

        budgetButton = new JButton();
        budgetButton.setText("Budget");
        budgetButton.addActionListener(e -> goToBudgetPanel());
        panel.add(budgetButton, "cell 0 1 3 1, w 100, alignx center");

        claimButton = new JButton();
        claimButton.setText("Claim");
        claimButton.setBackground(Color.BLACK);
        claimButton.setForeground(Color.WHITE);
        claimButton.addActionListener(e -> {
            refresh();
            clear();
        });
        panel.add(claimButton, "cell 0 1 3 1, w 100, alignx center");

        claimTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        claimTableModel.setColumnIdentifiers(new String[]{"Date", "Status", "Remark", "Activity", "Budget", "User", "Amount"});

        claimTable = new JTable(claimTableModel);
        claimTable.getTableHeader().setReorderingAllowed(false);
        claimTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                fillForm(claimTable.rowAtPoint(evt.getPoint()));
            }
        });
        panel.add(new JScrollPane(claimTable), "cell 0 2 3 1, gaptop 20, gapbottom 5, growx, h 200");

        approveButton = new JButton();
        approveButton.setText("Approve");
        approveButton.addActionListener(e -> {
            if (!formValidation()) {
                JOptionPane.showMessageDialog(null, "There Are Still Blank Forms");
            } else {
                approve();
            }
        });
        panel.add(approveButton, "cell 0 3 3 1, w 100, gapbottom 20, alignx right");

        rejectButton = new JButton();
        rejectButton.setText("Reject");
        rejectButton.addActionListener(e -> {
            if (!formValidation()) {
                JOptionPane.showMessageDialog(null, "There Are Still Blank Forms");
            } else {
                reject();
            }
        });
        panel.add(rejectButton, "cell 0 3 3 1, w 100, gapbottom 20, alignx right");

        remarkLabel = new JLabel();
        remarkLabel.setText("Remark");
        remarkLabel.setLabelFor(remarkTextField);
        panel.add(remarkLabel, "cell 0 4 1 1");

        remarkTextField = new JTextField();
        panel.add(new JScrollPane(remarkTextField), "cell 1 4 1 1, w 250");

        activityLabel = new JLabel();
        activityLabel.setText("Activity");
        activityLabel.setLabelFor(activityComboBox);
        panel.add(activityLabel, "cell 0 5 1 1");

        activityComboBox = new JComboBox<>();
        activityComboBox.setSelectedIndex(-1);
        panel.add(activityComboBox, "cell 1 5 1 1, w 250");

        budgetLabel = new JLabel();
        budgetLabel.setText("Budget");
        budgetLabel.setLabelFor(budgetComboBox);
        panel.add(budgetLabel, "cell 0 6 1 1");

        budgetComboBox = new JComboBox<>();
        budgetComboBox.setSelectedIndex(-1);
        panel.add(budgetComboBox, "cell 1 6 1 1, w 250");

        amountLabel = new JLabel();
        amountLabel.setText("Amount");
        amountLabel.setLabelFor(amountTextField);
        panel.add(amountLabel, "cell 0 7 1 1");

        amountTextField = new JTextField();
        amountTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });
        panel.add(amountTextField, "cell 1 7 1 1, w 250");

        clearButton = new JButton();
        clearButton.setText("Clear");
        clearButton.addActionListener(e -> {
            refresh();
            clear();
        });
        panel.add(clearButton, "cell 0 8 3 1, w 100, gaptop 10, alignx right");

        saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.addActionListener(e -> {
            if (!formValidation()) {
                JOptionPane.showMessageDialog(null, "There Are Still Blank Forms");
            } else {
                save();
            }
        });
        panel.add(saveButton, "cell 0 8 3 1, w 100, gaptop 10, alignx right");

        deleteButton = new JButton();
        deleteButton.setText("Delete");
        deleteButton.addActionListener(e -> {
            if (!formValidation()) {
                JOptionPane.showMessageDialog(null, "There Are Still Blank Forms");
            } else {
                delete();
            }
        });
        panel.add(deleteButton, "cell 0 8 3 1, w 100, gaptop 10, alignx right");
        refresh();
    }

    public static ClaimPanel getInstance() {
        return claimPanel == null ? claimPanel = new ClaimPanel() : claimPanel;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void clear() {
        remarkTextField.setText("");
        activityComboBox.setSelectedIndex(-1);
        budgetComboBox.setSelectedIndex(-1);
        amountTextField.setText("");
        currentClaim = null;
        approveButton.setVisible(false);
        rejectButton.setVisible(false);
    }

    public void refresh() {
        claimService = ClaimService.getInstance();
        userService = UserService.getInstance();
        activityService = ActivityService.getInstance();
        budgetService = BudgetService.getInstance();
        CodeMessageObject<ArrayList<Claim>> claimsCodeMessageObject = claimService.getAll();
        claimTableModel.setRowCount(0);
        activityComboBox.removeAllItems();
        budgetComboBox.removeAllItems();
        for (String description : GlobalVariable.activityDescriptions) {
            activityComboBox.addItem(description);
        }
        for (String description : GlobalVariable.budgetDescriptions) {
            budgetComboBox.addItem(description);
        }
        if (claimsCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, claimsCodeMessageObject.getMessage());
        } else {
            GlobalVariable.claims = claimsCodeMessageObject.getT();
            for (Claim claim : GlobalVariable.claims) {
                claim.setActivity(activityService.getById(claim.getActivityId()).getT());
                claim.setBudget(budgetService.getById(claim.getBudgetId()).getT());
                claim.setUser(userService.getById(claim.getUserId()).getT());
                claimTableModel.addRow(new Object[]{claim.getDate(), claim.getStatus(), claim.getRemark(), claim.getActivity().getDescription(), claim.getBudget().getDescription(), claim.getUser().getName(), claim.getAmount()});
            }
        }
        claimTable.revalidate();
        claimTable.repaint();
    }

    private Boolean formValidation() {
        return !remarkTextField.getText().trim().equals("") && activityComboBox.getSelectedIndex() >= 0 && budgetComboBox.getSelectedIndex() >= 0 && !amountTextField.getText().trim().equals("0");
    }

    public void setHello(String name) {
        helloLabel.setText(String.format("Hello, %s", name));
        GlobalVariable.currentUser = UserRepository.getInstance().getUserByName(name);
        user = GlobalVariable.currentUser;
        if(GlobalVariable.currentUser.getRole().equals("Officer")){
            budgetButton.setVisible(false);
            activityButton.setVisible(false);
        }
        else{
            budgetButton.setVisible(true);
            activityButton.setVisible(true);
        }
    }

    private void goToAuthenticationFrame() {
        mainFrame = MainFrame.getInstance();
        authenticationFrame = AuthenticationFrame.getInstance();
        loginPanel = LoginPanel.getInstance();
        clear();
        mainFrame.hide();
        authenticationFrame.show();
        authenticationFrame.changePanel(loginPanel.getPanel());
    }

    private void goToActivityPanel() {
        mainFrame = MainFrame.getInstance();
        activityPanel = ActivityPanel.getInstance();
        activityPanel.refresh();
        refresh();
        clear();
        budgetPanel.setHello(user.getName());
        mainFrame.changePanel(activityPanel.getPanel());
    }

    private void goToBudgetPanel() {
        mainFrame = MainFrame.getInstance();
        budgetPanel = BudgetPanel.getInstance();
        budgetPanel.refresh();
        refresh();
        clear();
        budgetPanel.setHello(user.getName());
        mainFrame.changePanel(budgetPanel.getPanel());
    }

    private void logout() {
        GlobalVariable.currentUser = null;
        goToAuthenticationFrame();
    }

    private void fillForm(Integer row) {
        claimService = ClaimService.getInstance();
        Timestamp date = (Timestamp) claimTableModel.getValueAt(row, 0);
        String status = (String) claimTableModel.getValueAt(row, 1);
        String remark = (String) claimTableModel.getValueAt(row, 2);
        String activityDescription = (String) claimTableModel.getValueAt(row, 3);
        String budgetDescription = (String) claimTableModel.getValueAt(row, 4);
        Integer amount = (Integer) claimTableModel.getValueAt(row, 6);
        CodeMessageObject<Claim> claimCodeMessageObject = claimService.getByDateAndStatusAndRemark(date, status, remark);
        if (claimCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, claimCodeMessageObject.getMessage());
        } else {
            currentClaim = claimCodeMessageObject.getT();
        }
        remarkTextField.setText(remark);
        activityComboBox.setSelectedIndex(GlobalVariable.activityDescriptions.indexOf(activityDescription));
        budgetComboBox.setSelectedIndex(GlobalVariable.budgetDescriptions.indexOf(budgetDescription));
        amountTextField.setText(amount.toString());
        if(status.equals("Confirm To Approve") && user.getRole().equals("Departement Head")){
            approveButton.setVisible(true);
            rejectButton.setVisible(true);
        }
        else{
            approveButton.setVisible(false);
            rejectButton.setVisible(false);
        }
    }

    private void save() {
        claimService = ClaimService.getInstance();
        Claim claim;
        String remark = remarkTextField.getText();
        String activityDescription = GlobalVariable.activityDescriptions.get(activityComboBox.getSelectedIndex());
        String budgetDescription = GlobalVariable.budgetDescriptions.get(budgetComboBox.getSelectedIndex());
        Integer amount = Integer.valueOf(amountTextField.getText());
        if (currentClaim == null) {
            claim = new Claim(GlobalVariable.docStatus.get(0), remark, amount);
        } else {
            claim = new Claim(currentClaim.getId(), currentClaim.getStatus(), remark, amount);
        }
        CodeMessageObject<Claim> claimCodeMessageObject = claimService.save(claim, activityDescription, budgetDescription, GlobalVariable.currentUser.getName());
        if (claimCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, claimCodeMessageObject.getMessage());
        }
        refresh();
        clear();
    }

    private void delete() {
        claimService = ClaimService.getInstance();
        CodeMessageObject<Claim> claimCodeMessageObject = claimService.delete(currentClaim.getId());
        if (claimCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, claimCodeMessageObject.getMessage());
        }
        refresh();
        clear();
    }

    private void approve() {
        claimService = ClaimService.getInstance();
        CodeMessageObject<Claim> claimCodeMessageObject = claimService.updateStatus(currentClaim.getId(), GlobalVariable.docStatus.get(1));
        if (claimCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, claimCodeMessageObject.getMessage());
        }
        refresh();
        clear();
    }

    private void reject() {
        claimService = ClaimService.getInstance();
        CodeMessageObject<Claim> claimCodeMessageObject = claimService.updateStatus(currentClaim.getId(), GlobalVariable.docStatus.get(2));
        if (claimCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, claimCodeMessageObject.getMessage());
        }
        refresh();
        clear();
    }
}
