import repository.ActivityRepository;
import ui.frame.AuthenticationFrame;
import ui.frame.MainFrame;
import ui.panel.*;
import util.DatabaseConnection;

public class Main {
    private static AuthenticationFrame authenticationFrame;
    private static MainFrame mainFrame;
    private static LoginPanel loginPanel;
    private static RegisterPanel registerPanel;
    private static ActivityPanel activityPanel;
    private static BudgetPanel budgetPanel;
    private static ClaimPanel claimPanel;
    private static ActivityRepository activityRepository;
    private static DatabaseConnection databaseConnection;

    public static void main(String[] args) {
        init();
        authenticationFrame.changePanel(loginPanel.getPanel());
        authenticationFrame.show();
//        mainFrame.changePanel(budgetPanel.getPanel());
//        mainFrame.show();
    }

    private static void init() {
        authenticationFrame = AuthenticationFrame.getInstance();
        loginPanel = LoginPanel.getInstance();
        registerPanel = RegisterPanel.getInstance();

        mainFrame = MainFrame.getInstance();
        activityPanel = ActivityPanel.getInstance();
        budgetPanel = BudgetPanel.getInstance();
        activityPanel = ActivityPanel.getInstance();

        databaseConnection = DatabaseConnection.getInstance();

        activityRepository = ActivityRepository.getInstance();
    }
}
