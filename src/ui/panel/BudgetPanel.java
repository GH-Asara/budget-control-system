package ui.panel;

import model.Budget;
import model.User;
import model.dto.CodeMessageObject;
import net.miginfocom.swing.MigLayout;
import repository.UserRepository;
import service.BudgetService;
import ui.frame.AuthenticationFrame;
import ui.frame.MainFrame;
import util.GlobalVariable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BudgetPanel {
    private static BudgetPanel budgetPanel = null;
    private MainFrame mainFrame;
    private ActivityPanel activityPanel;
    private ClaimPanel claimPanel;
    private AuthenticationFrame authenticationFrame;
    private LoginPanel loginPanel;
    private BudgetService budgetService;
    private JPanel panel;
    private JButton logoutButton, activityButton, budgetButton, claimButton, clearButton, saveButton, deleteButton;
    private JTable budgetTable;
    private DefaultTableModel budgetTableModel;
    private JLabel helloLabel, descriptionLabel, amountLabel;
    private JTextField descriptionTextField, amountTextField;
    private Budget currentBudget = null;
    private User user;

    private BudgetPanel() {
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
        budgetButton.setBackground(Color.BLACK);
        budgetButton.setForeground(Color.WHITE);
        budgetButton.addActionListener(e -> {
            clear();
            refresh();
        });
        panel.add(budgetButton, "cell 0 1 3 1, w 100, alignx center");

        claimButton = new JButton();
        claimButton.setText("Claim");
        claimButton.addActionListener(e -> goToClaimPanel());
        panel.add(claimButton, "cell 0 1 3 1, w 100, alignx center");

        budgetTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        budgetTableModel.setColumnIdentifiers(new String[]{"Description", "Amount"});

        budgetTable = new JTable(budgetTableModel);
        budgetTable.getTableHeader().setReorderingAllowed(false);
        budgetTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fillForm(budgetTable.rowAtPoint(evt.getPoint()));
            }
        });
        panel.add(new JScrollPane(budgetTable), "cell 0 2 3 1, gaptop 20, gapbottom 20, growx, h 200");
        refresh();

        descriptionLabel = new JLabel();
        descriptionLabel.setText("Description");
        descriptionLabel.setLabelFor(descriptionTextField);
        panel.add(descriptionLabel, "cell 0 3 1 1");

        descriptionTextField = new JTextField();
        panel.add(descriptionTextField, "cell 1 3 1 1, w 250");

        amountLabel = new JLabel();
        amountLabel.setText("Amount");
        amountLabel.setLabelFor(amountTextField);
        panel.add(amountLabel, "cell 0 4 1 1");

        amountTextField = new JTextField();
        amountTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });
        panel.add(amountTextField, "cell 1 4 1 1, w 250");

        clearButton = new JButton();
        clearButton.setText("Clear");
        clearButton.addActionListener(e -> {
            clear();
            refresh();
        });
        panel.add(clearButton, "cell 0 5 3 1, w 100, gaptop 10, alignx right");

        saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.addActionListener(e -> {
            if (!formValidation()) {
                JOptionPane.showMessageDialog(null, "There Are Still Blank Forms");
            } else {
                save();
            }
        });
        panel.add(saveButton, "cell 0 5 3 1, w 100, gaptop 10, alignx right");

        deleteButton = new JButton();
        deleteButton.setText("Delete");
        deleteButton.addActionListener(e -> {
            if (!formValidation()) {
                JOptionPane.showMessageDialog(null, "There Are Still Blank Forms");
            } else {
                delete();
            }
        });
        panel.add(deleteButton, "cell 0 5 3 1, w 100, gaptop 10, alignx right");
    }

    public static BudgetPanel getInstance() {
        return budgetPanel == null ? budgetPanel = new BudgetPanel() : budgetPanel;
    }

    public JPanel getPanel() {
        return panel;
    }

    private void clear() {
        descriptionTextField.setText("");
        amountTextField.setText("");
        currentBudget = null;
    }

    public void refresh() {
        budgetService = BudgetService.getInstance();
        CodeMessageObject<ArrayList<Budget>> budgetsCodeMessageObject = budgetService.getAll();
        budgetTableModel.setRowCount(0);
        GlobalVariable.budgetDescriptions = new ArrayList<>();
        if (budgetsCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, budgetsCodeMessageObject.getMessage());
        } else {
            GlobalVariable.budgets = budgetsCodeMessageObject.getT();
            for (Budget budget : GlobalVariable.budgets) {
                GlobalVariable.budgetDescriptions.add(budget.getDescription());
                budgetTableModel.addRow(new Object[]{budget.getDescription(), budget.getAmount()});
            }
        }
        budgetTable.revalidate();
        budgetTable.repaint();
    }

    private Boolean formValidation() {
        return !descriptionTextField.getText().trim().equals("") && !amountTextField.getText().trim().equals("");
    }

    public void setHello(String name) {
        helloLabel.setText(String.format("Hello, %s", name));
        GlobalVariable.currentUser = UserRepository.getInstance().getUserByName(name);
        user = GlobalVariable.currentUser;
        if(GlobalVariable.currentUser.getRole().equals("Officer")){
            budgetButton.setVisible(false);
            activityButton.setVisible(false);
            goToClaimPanel();
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
        activityPanel.setHello(GlobalVariable.currentUser.getName());
        clear();
        refresh();
        mainFrame.changePanel(activityPanel.getPanel());
    }

    private void goToClaimPanel() {
        mainFrame = MainFrame.getInstance();
        claimPanel = ClaimPanel.getInstance();
        GlobalVariable.currentUser = user;
        claimPanel.setHello(GlobalVariable.currentUser.getName());
        claimPanel.refresh();
        claimPanel.clear();
        clear();
        refresh();
        mainFrame.changePanel(claimPanel.getPanel());
    }

    private void logout() {
        GlobalVariable.currentUser = null;
        goToAuthenticationFrame();
    }

    private void fillForm(Integer row) {
        budgetService = BudgetService.getInstance();
        String description = (String) budgetTableModel.getValueAt(row, 0);
        Integer amount = (Integer) budgetTableModel.getValueAt(row, 1);
        CodeMessageObject<Budget> budgetCodeMessageObject = budgetService.getByDescription(description);
        if (budgetCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, budgetCodeMessageObject.getMessage());
        } else {
            currentBudget = budgetCodeMessageObject.getT();
        }
        descriptionTextField.setText(description);
        amountTextField.setText(String.valueOf(amount));
    }

    private void save() {
        budgetService = BudgetService.getInstance();
        Budget budget;
        String description = descriptionTextField.getText();
        Integer amount = Integer.valueOf(amountTextField.getText());
        System.out.println(currentBudget);
        if (currentBudget == null) {
            budget = new Budget(description, amount);
        } else {
            budget = new Budget(currentBudget.getId(), description, amount);
        }
        CodeMessageObject<Budget> budgetCodeMessageObject = budgetService.save(budget);
        if (budgetCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, budgetCodeMessageObject.getMessage());
        }
        clear();
        refresh();
    }

    private void delete() {
        budgetService = BudgetService.getInstance();
        CodeMessageObject<Budget> budgetCodeMessageObject = budgetService.delete(currentBudget.getId());
        if (budgetCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, budgetCodeMessageObject.getMessage());
        }
        clear();
        refresh();
    }
}
