package ui.panel;

import model.Activity;
import model.User;
import model.dto.CodeMessageObject;
import net.miginfocom.swing.MigLayout;
import repository.UserRepository;
import service.ActivityService;
import service.UserService;
import ui.frame.AuthenticationFrame;
import ui.frame.MainFrame;
import util.GlobalVariable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ActivityPanel {
    private static ActivityPanel activityPanel = null;
    private MainFrame mainFrame;
    private BudgetPanel budgetPanel;
    private ClaimPanel claimPanel;
    private AuthenticationFrame authenticationFrame;
    private LoginPanel loginPanel;
    private ActivityService activityService;
    private JPanel panel;
    private JButton logoutButton, activityButton, budgetButton, claimButton, clearButton, saveButton, deleteButton;
    private JTable activityTable;
    private DefaultTableModel activityTableModel;
    private JLabel helloLabel, descriptionLabel, activityTypeLabel;
    private JTextField descriptionTextField;
    private JComboBox activityTypeComboBox;
    private Activity currentActivity = null;

    private ActivityPanel() {
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
        activityButton.setBackground(Color.BLACK);
        activityButton.setForeground(Color.WHITE);
        activityButton.addActionListener(e -> {
            clear();
            refresh();
        });
        panel.add(activityButton, "cell 0 1 3 1, w 100, alignx center");

        budgetButton = new JButton();
        budgetButton.setText("Budget");
        budgetButton.addActionListener(e -> goToBudgetPanel());
        panel.add(budgetButton, "cell 0 1 3 1, w 100, alignx center");

        claimButton = new JButton();
        claimButton.setText("Claim");
        claimButton.addActionListener(e -> goToClaimPanel());
        panel.add(claimButton, "cell 0 1 3 1, w 100, alignx center");

        activityTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        activityTableModel.setColumnIdentifiers(new String[]{"Description", "Activity Type"});

        activityTable = new JTable(activityTableModel);
        activityTable.getTableHeader().setReorderingAllowed(false);
        activityTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                fillForm(activityTable.rowAtPoint(evt.getPoint()));
            }
        });
        panel.add(new JScrollPane(activityTable), "cell 0 2 3 1, gaptop 20, gapbottom 20, growx, h 200");

        descriptionLabel = new JLabel();
        descriptionLabel.setText("Desription");
        descriptionLabel.setLabelFor(descriptionTextField);
        panel.add(descriptionLabel, "cell 0 3 1 1");

        descriptionTextField = new JTextField();
        panel.add(descriptionTextField, "cell 1 3 1 1, w 250");

        activityTypeLabel = new JLabel();
        activityTypeLabel.setText("Activity Type");
        activityTypeLabel.setLabelFor(activityTypeLabel);
        panel.add(activityTypeLabel, "cell 0 4 1 1");

        activityTypeComboBox = new JComboBox<>(GlobalVariable.activityTypes.toArray());
        activityTypeComboBox.setSelectedIndex(-1);
        panel.add(activityTypeComboBox, "cell 1 4 1 1, w 250");

        clearButton = new JButton();
        clearButton.setText("Clear");
        clearButton.addActionListener(e -> {
            refresh();
            clear();
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
        refresh();
        clear();
    }

    public static ActivityPanel getInstance() {
        return activityPanel == null ? activityPanel = new ActivityPanel() : activityPanel;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void clear() {
        descriptionTextField.setText("");
        activityTypeComboBox.setSelectedIndex(-1);
        currentActivity = null;
    }

    public void refresh() {
        activityService = ActivityService.getInstance();
        CodeMessageObject<ArrayList<Activity>> activitiesCodeMessageObject = activityService.getAll();
        activityTableModel.setRowCount(0);
        GlobalVariable.activityDescriptions = new ArrayList<>();
        if (activitiesCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, activitiesCodeMessageObject.getMessage());
        } else {
            GlobalVariable.activities = activitiesCodeMessageObject.getT();
            for (Activity activity : GlobalVariable.activities) {
                GlobalVariable.activityDescriptions.add(activity.getDescription());
                activityTableModel.addRow(new Object[]{activity.getDescription(), activity.getActivityType()});
            }
        }
        activityTable.revalidate();
        activityTable.repaint();
    }

    private Boolean formValidation() {
        return !descriptionTextField.getText().trim().equals("") && activityTypeComboBox.getSelectedIndex() >= 0;
    }

    public void setHello(String name) {
        helloLabel.setText(String.format("Hello, %s", name));
        GlobalVariable.currentUser = UserRepository.getInstance().getUserByName(name);
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

    private void goToBudgetPanel() {
        mainFrame = MainFrame.getInstance();
        budgetPanel = BudgetPanel.getInstance();
        budgetPanel.refresh();
        refresh();
        clear();
        mainFrame.changePanel(budgetPanel.getPanel());
    }

    private void goToClaimPanel() {
        mainFrame = MainFrame.getInstance();
        claimPanel = ClaimPanel.getInstance();
        claimPanel.refresh();
        claimPanel.clear();
        refresh();
        clear();
        mainFrame.changePanel(claimPanel.getPanel());
    }

    private void logout() {
        GlobalVariable.currentUser = null;
        goToAuthenticationFrame();
    }

    private void fillForm(Integer row) {
        activityService = ActivityService.getInstance();
        String description = (String) activityTableModel.getValueAt(row, 0);
        String activityType = (String) activityTableModel.getValueAt(row, 1);
        CodeMessageObject<Activity> activityCodeMessageObject = activityService.getByDescription(description);
        if (activityCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, activityCodeMessageObject.getMessage());
        } else {
            currentActivity = activityCodeMessageObject.getT();
        }
        descriptionTextField.setText(description);
        activityTypeComboBox.setSelectedIndex(GlobalVariable.activityTypes.indexOf(activityType));
    }

    private void save() {
        activityService = ActivityService.getInstance();
        Activity activity;
        String description = descriptionTextField.getText();
        String activityType = GlobalVariable.activityTypes.get(activityTypeComboBox.getSelectedIndex());
        if (currentActivity == null) {
            activity = new Activity(description, activityType);
        } else {
            activity = new Activity(currentActivity.getId(), description, activityType);
        }
        CodeMessageObject<Activity> activityCodeMessageObject = activityService.save(activity);
        if (activityCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, activityCodeMessageObject.getMessage());
        }
        refresh();
        clear();
    }

    private void delete() {
        activityService = ActivityService.getInstance();
        CodeMessageObject<Activity> activityCodeMessageObject = activityService.delete(currentActivity.getId());
        if (activityCodeMessageObject.getCode() == 400) {
            JOptionPane.showMessageDialog(null, activityCodeMessageObject.getMessage());
        }
        refresh();
        clear();
    }
}
