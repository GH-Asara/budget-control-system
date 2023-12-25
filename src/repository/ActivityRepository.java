package repository;

import model.Activity;
import util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActivityRepository {
    private static ActivityRepository activityRepository;
    private DatabaseConnection databaseConnection;

    private ActivityRepository() {
    }

    public static ActivityRepository getInstance() {
        return activityRepository == null ? activityRepository = new ActivityRepository() : activityRepository;
    }

    public ArrayList<Activity> getAllActivity() {
        databaseConnection = DatabaseConnection.getInstance();
        ArrayList<Activity> activities = new ArrayList<>();
        String query = "select szActivityId, szDescription, szActivityType\n" +
                "from activity;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                activities.add(new Activity(resultSet.getString("szActivityId"), resultSet.getString("szDescription"), resultSet.getString("szActivityType")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    public Activity getActivityById(String szActivityId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "select szActivityId, szDescription, szActivityType\n" +
                "from activity\n" +
                "where szActivityId = ?;";
        Activity activity = null;
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szActivityId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                activity = new Activity(resultSet.getString("szActivityId"), resultSet.getString("szDescription"), resultSet.getString("szActivityType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activity;
    }

    public Activity createActivity(Activity activity) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "insert into activity(szActivityId, szDescription, szActivityType)\n" +
                "values (?, ?, ?);";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, activity.getId());
            preparedStatement.setString(2, activity.getDescription());
            preparedStatement.setString(3, activity.getActivityType());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activity;
    }

    public Activity updateActivity(Activity activity) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "update activity\n" +
                "set szDescription = ?, szActivityType = ?\n" +
                "where szActivityId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, activity.getDescription());
            preparedStatement.setString(2, activity.getActivityType());
            preparedStatement.setString(3, activity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activity;
    }

    public void deleteActivityById(String szActivityId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "delete from activity\n" +
                "where szActivityId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szActivityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Activity getActivityByDescription(String szDescription) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "select szActivityId, szDescription, szActivityType\n" +
                "from activity\n" +
                "where szDescription = ?;";
        Activity activity = null;
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szDescription);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                activity = new Activity(resultSet.getString("szActivityId"), resultSet.getString("szDescription"), resultSet.getString("szActivityType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activity;
    }
}
