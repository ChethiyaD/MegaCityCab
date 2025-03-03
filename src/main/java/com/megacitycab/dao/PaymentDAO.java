package com.megacitycab.dao;

import com.megacitycab.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class PaymentDAO {
    private Connection conn;

    public PaymentDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean savePayment(int bookingId, String customerUsername, String cardNumber, String cardHolderName, String expiryDate, String cvv, double totalAmount) {
        // ✅ Fixed: Changed `payment` to `payments`
        String query = "INSERT INTO payments (booking_id, customer_username, card_number, card_holder_name, expiry_date, cvv, total_amount, payment_status, payment_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.setString(2, customerUsername);
            stmt.setString(3, cardNumber);
            stmt.setString(4, cardHolderName);
            stmt.setString(5, expiryDate);
            stmt.setString(6, cvv);
            stmt.setDouble(7, totalAmount);
            stmt.setString(8, "Completed"); // Mark payment as completed
            stmt.setTimestamp(9, new Timestamp(new Date().getTime())); // Current Timestamp

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
