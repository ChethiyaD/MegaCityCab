package com.megacitycab.dao;

import com.megacitycab.models.Booking;
import com.megacitycab.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private Connection conn;

    public BookingDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create a booking with estimated bill
    public boolean createBooking(Booking booking) {
        String query = "INSERT INTO bookings (customer_username, car_id, driver_username, pickup_location, dropoff_location, status, estimated_bill) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, booking.getCustomerUsername());
            stmt.setInt(2, booking.getCarId());
            stmt.setString(3, booking.getDriverUsername());
            stmt.setString(4, booking.getPickupLocation());
            stmt.setString(5, booking.getDropoffLocation());
            stmt.setString(6, booking.getStatus());
            stmt.setDouble(7, booking.getEstimatedBill()); // Store the estimated bill in DB
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all bookings for a customer
    public List<Booking> getCustomerBookings(String customerUsername) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE customer_username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_username"),
                        rs.getInt("car_id"),
                        rs.getString("driver_username"),
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("status"),
                        rs.getDouble("estimated_bill") // Retrieve the estimated bill (LKR)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Get bookings with car & driver details (Including images)
    public List<Booking> getBookingsByCustomer(String username) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.id, b.customer_username, b.car_id, c.car_number, c.car_name, c.image AS car_image, " +
                "u.username AS driver_username, u.profile_picture AS driver_image, " +
                "b.pickup_location, b.dropoff_location, b.status, b.estimated_bill " +  // Added estimated_bill
                "FROM bookings b " +
                "JOIN cars c ON b.car_id = c.id " +
                "LEFT JOIN users u ON b.driver_username = u.username " +
                "WHERE b.customer_username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_username"),
                        rs.getInt("car_id"),
                        rs.getString("car_number"),  // Car number
                        rs.getString("car_name"),    // Car name
                        rs.getString("car_image"),   // Car image
                        rs.getString("driver_username"),  // Driver username
                        rs.getString("driver_image"),  // Driver image
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("status"),
                        rs.getDouble("estimated_bill")  // Estimated bill (LKR)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Confirm booking: update status and bill amount
    public boolean confirmBooking(int bookingId, double billAmount) {
        String query = "UPDATE bookings SET status = 'Confirmed', estimated_bill = ? WHERE id = ? AND status = 'Pending'";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, billAmount);
            stmt.setInt(2, bookingId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Now mark the driver as unavailable after confirming the booking
                String driverUsername = getDriverUsernameByBookingId(bookingId); // Retrieve the driver username from the booking
                markDriverAsUnavailable(driverUsername);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mark the driver as unavailable after booking
    public void markDriverAsUnavailable(String driverUsername) {
        String query = "UPDATE users SET status = 'Unavailable' WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, driverUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Retrieve the driver of the booking by booking ID
    private String getDriverUsernameByBookingId(int bookingId) {
        String query = "SELECT driver_username FROM bookings WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("driver_username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get bookings assigned to the driver (only pending)
    public List<Booking> getBookingsByDriver(String driverUsername) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.id, b.customer_username, b.car_id, c.car_number, c.car_name, c.image AS car_image, " +
                "u.username AS driver_username, u.profile_picture AS driver_image, " +
                "b.pickup_location, b.dropoff_location, b.status " +
                "FROM bookings b " +
                "JOIN cars c ON b.car_id = c.id " +
                "LEFT JOIN users u ON b.driver_username = u.username " +
                "WHERE b.driver_username = ? AND b.status = 'Pending'"; // Assuming we want to show only "Pending" bookings

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, driverUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_username"),
                        rs.getInt("car_id"),
                        rs.getString("car_number"),  // Car number
                        rs.getString("car_name"),    // Car name
                        rs.getString("car_image"),   // Car image
                        rs.getString("driver_username"),  // Driver username
                        rs.getString("driver_image"),  // Driver image
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("status"),
                        0.0  // Placeholder for estimatedBill (we can remove this if the driver manually inputs the amount)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Get a specific car's fare per kilometer (for the driver's assignment page)
    public double getFarePerKm(int carId) {
        String query = "SELECT fare_per_km FROM cars WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("fare_per_km");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Default fare if not found
    }
}
