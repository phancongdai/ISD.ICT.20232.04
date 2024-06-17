package controller;

import entity.db.AIMSDB;
import entity.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationController extends  BaseController{
    public User loginWithUsernameAndPassword(String username, String password) throws SQLException {
        User user = User.checkValidPassword(username, password);
        if (user == null) {
            System.out.println("Incorrect username or password");
            return null;
        } else {
            System.out.println("Login successful");
            return user;
        }
    }

    public int registerNewUser(String email, String name, String password) throws SQLException {
        Connection connect = AIMSDB.getConnection();
        try {
            // Check exist account
            String checkExistSql = "SELECT * FROM User WHERE name = ?";
            PreparedStatement statement = connect.prepareStatement(checkExistSql);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return 0;
            } else {
                String insertSql = "INSERT INTO User (email, name, password, address, phone) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement prepare = connect.prepareStatement(insertSql);
                prepare.setString(1, email);
                prepare.setString(2, name);
                prepare.setString(3, password);
                prepare.setString(4, "21 Hang Bai");
                prepare.setString(5, "0123456789");
                prepare.executeUpdate();

                return 1;
            }
        } catch (Exception e) {
            System.out.println("Register user error: " + e.getMessage());
            return 0;
        }
    }
}
