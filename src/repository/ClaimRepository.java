package repository;

import model.Claim;
import util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ClaimRepository {
    private static ClaimRepository claimRepository = null;
    private DatabaseConnection databaseConnection;

    public ClaimRepository() {
    }

    public static ClaimRepository getInstance() {
        return claimRepository == null ? claimRepository = new ClaimRepository() : claimRepository;
    }

    public ArrayList<Claim> getAllClaim() {
        databaseConnection = DatabaseConnection.getInstance();
        ArrayList<Claim> claims = new ArrayList<>();
        String query = "select szDocumentId, dtmDocDate, szDocStatus, szRemark, szActivityId, szBudgetId, szApprover, szApprovalId, decAmount\n" +
                "from claim;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                claims.add(new Claim(resultSet.getString("szDocumentId"), resultSet.getTimestamp("dtmDocDate"), resultSet.getString("szDocStatus"), resultSet.getString("szRemark"), resultSet.getString("szActivityId"), resultSet.getString("szBudgetId"), resultSet.getString("szApprover"), resultSet.getString("szApprovalId"), resultSet.getInt("decAmount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public Claim getClaimById(String szDocumentId) {
        databaseConnection = DatabaseConnection.getInstance();
        Claim claim = null;
        String query = "select szDocumentId, dtmDocDate, szDocStatus, szRemark, szActivityId, szBudgetId, szApprover, szApprovalId, decAmount\n" +
                "from claim\n" +
                "where szDocumentId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szDocumentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                claim = new Claim(resultSet.getString("szDocumentId"), resultSet.getTimestamp("dtmDocDate"), resultSet.getString("szDocStatus"), resultSet.getString("szRemark"), resultSet.getString("szActivityId"), resultSet.getString("szBudgetId"), resultSet.getString("szApprover"), resultSet.getString("szApprovalId"), resultSet.getInt("decAmount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claim;
    }

    public Claim createClaim(Claim claim) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "insert into claim(szDocumentId, dtmDocDate, szDocStatus, szRemark, szActivityId, szBudgetId, szApprover, szApprovalId, decAmount)\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, claim.getId());
            preparedStatement.setTimestamp(2, claim.getDate());
            preparedStatement.setString(3, claim.getStatus());
            preparedStatement.setString(4, claim.getRemark());
            preparedStatement.setString(5, claim.getActivityId());
            preparedStatement.setString(6, claim.getBudgetId());
            preparedStatement.setString(7, claim.getUserId());
            preparedStatement.setString(8, claim.getApprovalId());
            preparedStatement.setInt(9, claim.getAmount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claim;
    }

    public Claim updateClaim(Claim claim) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "update claim\n" +
                "set dtmDocDate = ?, szDocStatus = ?, szRemark = ?, szActivityId = ?, szBudgetId = ?, szApprover = ?, szApprovalId = ?, decAmount = ?\n" +
                "where szDocumentId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setTimestamp(1, claim.getDate());
            preparedStatement.setString(2, claim.getStatus());
            preparedStatement.setString(3, claim.getRemark());
            preparedStatement.setString(4, claim.getActivityId());
            preparedStatement.setString(5, claim.getBudgetId());
            preparedStatement.setString(6, claim.getUserId());
            preparedStatement.setString(7, claim.getApprovalId());
            preparedStatement.setInt(8, claim.getAmount());
            preparedStatement.setString(9, claim.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claim;
    }

    public void deleteById(String szDocumentId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "delete from claim\n" +
                "where szDocumentId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szDocumentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Claim getClaimByDateAndStatusAndRemark(Timestamp dtmDocDate, String szDocStatus, String szRemark) {
        databaseConnection = DatabaseConnection.getInstance();
        Claim claim = null;
        String query = "select szDocumentId, dtmDocDate, szDocStatus, szRemark, szActivityId, szBudgetId, szApprover, szApprovalId, decAmount\n" +
                "from claim\n" +
                "where dtmDocDate = ? and szDocStatus = ? and szRemark = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setTimestamp(1, dtmDocDate);
            preparedStatement.setString(2, szDocStatus);
            preparedStatement.setString(3, szRemark);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                claim = new Claim(resultSet.getString("szDocumentId"), resultSet.getTimestamp("dtmDocDate"), resultSet.getString("szDocStatus"), resultSet.getString("szRemark"), resultSet.getString("szActivityId"), resultSet.getString("szBudgetId"), resultSet.getString("szApprover"), resultSet.getString("szApprovalId"), resultSet.getInt("decAmount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claim;
    }

    public void updateClaimStatus(String szDocumentId, Timestamp dtmDocDate, String szDocStatus, String approvalId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "update claim\n" +
                "\tset dtmDocDate = ?, szDocStatus = ?, szApprovalId = ?\n" +
                "\twhere szDocumentId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setTimestamp(1, dtmDocDate);
            preparedStatement.setString(2, szDocStatus);
            preparedStatement.setString(3, approvalId);
            preparedStatement.setString(4, szDocumentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
