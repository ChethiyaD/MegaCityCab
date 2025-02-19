package com.megacitycab.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Database connection successful!");
                conn.close(); // Close connection after test
            } else {
                System.out.println("❌ Database connection failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
