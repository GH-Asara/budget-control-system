package repository;

import model.User;
import util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepository {
    private static UserRepository userRepository = null;
    private DatabaseConnection databaseConnection;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return userRepository == null ? userRepository = new UserRepository() : userRepository;
    }

    public ArrayList<User> getAllUser() {
        databaseConnection = DatabaseConnection.getInstance();
        ArrayList<User> users = new ArrayList<>();
        String query = "select szUserId, szName, szUsername, szPassword, szRole\n" +
                "from bcs_user;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getString("szUserId"), resultSet.getString("szName"), resultSet.getString("szUsername"), resultSet.getString("szPassword"), resultSet.getString("szRole")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserById(String szUserId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "select szUserId, szName, szUsername, szPassword, szRole\n" +
                "from bcs_user\n" +
                "where szUserId = ?;";
        User user = null;
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("szUserId"), resultSet.getString("szName"), resultSet.getString("szUsername"), resultSet.getString("szPassword"), resultSet.getString("szRole"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User createUser(User user) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "insert into bcs_user(szUserId, szName, szUsername, szPassword, szRole)\n" +
                "values (?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User updateUser(User user) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "update bcs_user\n" +
                "set szName = ?, szUsername = ?, szPassword = ?, szRole = ?\n" +
                "where szUserId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUserById(String szUserId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "delete from bcs_user\n" +
                "where szUserId = ?;";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szUserId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByName(String szName) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "select szUserId, szName, szUsername, szPassword, szRole\n" +
                "from bcs_user\n" +
                "where szName = ?;";
        User user = null;
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("szUserId"), resultSet.getString("szName"), resultSet.getString("szUsername"), resultSet.getString("szPassword"), resultSet.getString("szRole"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByUsername(String szUsername) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "select szUserId, szName, szUsername, szPassword, szRole\n" +
                "from bcs_user\n" +
                "where szUsername = ?;";
        User user = null;
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("szUserId"), resultSet.getString("szName"), resultSet.getString("szUsername"), resultSet.getString("szPassword"), resultSet.getString("szRole"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getUserRoleById(String szUserId) {
        databaseConnection = DatabaseConnection.getInstance();
        String query = "select szRole\n" +
                "from bcs_user\n" +
                "where szUserId = ?;";
        String role = null;
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, szUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("szRole");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
}
