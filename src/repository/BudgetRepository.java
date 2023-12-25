package repository;

import model.Budget;
import util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BudgetRepository {
    private static BudgetRepository budgetRepository;
    private DatabaseConnection databaseConnection;

    private BudgetRepository() {
    }

    public static BudgetRepository getInstance() {
        return budgetRepository == null ? budgetRepository = new BudgetRepository() : budgetRepository;
    }

    public ArrayList<Budget> getAllBudget() {
        databaseConnection = DatabaseConnection.getInstance();
        ArrayList<Budget> budgets = new ArrayList<>();
        String query = "select szBudgetId, szDescription, decAmount\n" +
                "from budgetmaster;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                budgets.add(new Budget(resultSet.getString("szBudgetId"), resultSet.getString("szDescription"), resultSet.getInt("decAmount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgets;
    }

    public Budget getBudgetById(String szBudgetId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "select szBudgetId, szDescription, decAmount\n" +
                "from budgetmaster\n" +
                "where szBudgetId = ?;";
        Budget budget = null;
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szBudgetId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                budget = new Budget(resultSet.getString("szBudgetId"), resultSet.getString("szDescription"), resultSet.getInt("decAmount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budget;
    }

    public Budget createBudget(Budget budget) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "insert into budgetmaster(szBudgetId, szDescription, decAmount)\n" +
                "values (?, ?, ?);";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, budget.getId());
            preparedStatement.setString(2, budget.getDescription());
            preparedStatement.setDouble(3, budget.getAmount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budget;
    }

    public Budget updateBudget(Budget budget) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "update budgetmaster\n" +
                "set szDescription = ?, decAmount = ?\n" +
                "where szBudgetId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, budget.getDescription());
            preparedStatement.setDouble(2, budget.getAmount());
            preparedStatement.setString(3, budget.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budget;
    }

    public void deleteBudgetById(String szBudgetId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "delete from budgetmaster\n" +
                "where szBudgetId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szBudgetId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Budget getBudgetByDescription(String szDescription) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "select szBudgetId, szDescription, decAmount\n" +
                "from budgetmaster\n" +
                "where szDescription = ?;";
        Budget budget = null;
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szDescription);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                budget = new Budget(resultSet.getString("szBudgetId"), resultSet.getString("szDescription"), resultSet.getInt("decAmount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budget;
    }
}
