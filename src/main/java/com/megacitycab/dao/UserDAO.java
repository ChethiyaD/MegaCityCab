package com.megacitycab.dao;

import com.megacitycab.models.User;
import com.megacitycab.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection conn;

    public UserDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Authenticate user (Fix: No more `drivers` table)
    public User authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        rs.getString("profile_picture"),
                        rs.getInt("experience"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Register new customer (No experience/status needed)
    public boolean registerUser(User user) {
        if (isUsernameTaken(user.getUsername())) {
            return false;
        }

        String query = "INSERT INTO users (username, password, role, name, address, phone, nic, profile_picture, experience, status) " +
                "VALUES (?, ?, 'customer', ?, ?, ?, ?, ?, 0, NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getNic());
            stmt.setString(7, user.getProfilePicture());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Register driver (Fix: No `drivers` table)
    public boolean registerDriver(User driver) {
        if (isUsernameTaken(driver.getUsername())) {
            System.out.println("Error: Username already exists!");
            return false;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String password = driver.getPassword();
            if (password == null || password.trim().isEmpty()) {
                password = "default123";  // Fix: Ensure password is always set
            }

            // ✅ Insert into `users` table only (NO drivers table anymore)
            String query = "INSERT INTO users (username, password, role, name, address, phone, nic, profile_picture, experience, status) " +
                    "VALUES (?, ?, 'driver', ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, driver.getUsername());
                stmt.setString(2, password);
                stmt.setString(3, driver.getName());
                stmt.setString(4, driver.getAddress() != null ? driver.getAddress() : "");
                stmt.setString(5, driver.getPhone());
                stmt.setString(6, driver.getNic() != null ? driver.getNic() : "");
                stmt.setString(7, driver.getProfilePicture() != null ? driver.getProfilePicture() : "default.png");
                stmt.setInt(8, driver.getExperience());
                stmt.setString(9, driver.getStatus() != null ? driver.getStatus() : "Available");
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Get all drivers (Fix: No `drivers` table, fetch from `users`)
    public List<User> getAllDrivers() {
        List<User> drivers = new ArrayList<>();
        String query = "SELECT username, password, role, name, address, phone, nic, profile_picture, experience, status " +
                "FROM users WHERE role = 'driver'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User driver = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        rs.getString("profile_picture"),
                        rs.getInt("experience"),
                        rs.getString("status")
                );
                drivers.add(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    // ✅ Delete driver (Only from `users` table)
    public boolean deleteDriver(String username) {
        String query = "DELETE FROM users WHERE username = ? AND role = 'driver'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Check if username already exists
    public boolean isUsernameTaken(String username) {
        String query = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
