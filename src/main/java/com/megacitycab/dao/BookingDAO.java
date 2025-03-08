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


    public boolean createBooking(Booking booking) {
        String query = "INSERT INTO bookings (customer_username, car_id, driver_username, pickup_location, dropoff_location, status, estimated_bill) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateCarQuery = "UPDATE cars SET availability = 0 WHERE id = ?"; // Mark car as unavailable

        try (PreparedStatement stmt = conn.prepareStatement(query);
             PreparedStatement updateStmt = conn.prepareStatement(updateCarQuery)) {

            stmt.setString(1, booking.getCustomerUsername());
            stmt.setInt(2, booking.getCarId());
            stmt.setString(3, booking.getDriverUsername());
            stmt.setString(4, booking.getPickupLocation());
            stmt.setString(5, booking.getDropoffLocation());
            stmt.setString(6, booking.getStatus());

            // Store 0.0 instead of "Calculating..." to avoid the MySQL error
            stmt.setDouble(7, 0.0);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                // Update the car availability
                updateStmt.setInt(1, booking.getCarId());
                updateStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }





    public List<Booking> getCustomerBookings(String customerUsername) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.id, b.customer_username, b.car_id, c.car_number, c.car_name, c.image AS car_image, " +
                "u.username AS driver_username, u.profile_picture AS driver_image, " +
                "b.pickup_location, b.dropoff_location, b.status, b.estimated_bill, b.distance " +
                "FROM bookings b " +
                "JOIN cars c ON b.car_id = c.id " +
                "LEFT JOIN users u ON b.driver_username = u.username " +
                "WHERE b.customer_username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_username"),
                        rs.getInt("car_id"),
                        rs.getString("car_number"),
                        rs.getString("car_name"),
                        rs.getString("car_image"),
                        rs.getString("driver_username"),
                        rs.getString("driver_image"),
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("status"),
                        rs.getDouble("estimated_bill"),
                        rs.getDouble("distance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }


    public List<Booking> getBookingsByCustomer(String username) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.id, b.customer_username, b.car_id, c.car_number, c.car_name, c.image AS car_image, " +
                "u.username AS driver_username, u.profile_picture AS driver_image, " +
                "b.pickup_location, b.dropoff_location, b.status, b.estimated_bill, b.distance " +
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
                        rs.getString("car_number"),
                        rs.getString("car_name"),
                        rs.getString("car_image"),
                        rs.getString("driver_username"),
                        rs.getString("driver_image"),
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("status"),
                        rs.getDouble("estimated_bill"),
                        rs.getDouble("distance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }


    public boolean confirmBooking(int bookingId, double billAmount, double distance) {
        String query = "UPDATE bookings SET status = 'Confirmed', estimated_bill = ?, distance = ? WHERE id = ? AND status = 'Pending'";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, billAmount);
            stmt.setDouble(2, distance);
            stmt.setInt(3, bookingId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                String driverUsername = getDriverUsernameByBookingId(bookingId);
                markDriverAsUnavailable(driverUsername);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void markDriverAsUnavailable(String driverUsername) {
        String query = "UPDATE users SET status = 'Unavailable' WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, driverUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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


    public List<Booking> getBookingsByDriver(String driverUsername) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.id, b.customer_username, b.car_id, c.car_number, c.car_name, c.image AS car_image, " +
                "u.username AS driver_username, u.profile_picture AS driver_image, " +
                "b.pickup_location, b.dropoff_location, b.status, b.estimated_bill, b.distance " +
                "FROM bookings b " +
                "JOIN cars c ON b.car_id = c.id " +
                "LEFT JOIN users u ON b.driver_username = u.username " +
                "WHERE b.driver_username = ? AND b.status = 'Pending'";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, driverUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_username"),
                        rs.getInt("car_id"),
                        rs.getString("car_number"),
                        rs.getString("car_name"),
                        rs.getString("car_image"),
                        rs.getString("driver_username"),
                        rs.getString("driver_image"),
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("status"),
                        rs.getDouble("estimated_bill"),
                        rs.getDouble("distance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }


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
        return 0.0;
    }


    public boolean cancelBooking(int bookingId) {
        String updateBookingQuery = "UPDATE bookings SET status = ? WHERE id = ?";
        String updateCarQuery = "UPDATE cars SET availability = 1 WHERE id = (SELECT car_id FROM bookings WHERE id = ?)";

        try (PreparedStatement stmt = conn.prepareStatement(updateBookingQuery);
             PreparedStatement carStmt = conn.prepareStatement(updateCarQuery)) {

            // Update booking status to "Cancelled"
            stmt.setString(1, "Cancelled"); // Use "Cancelled" (correct ENUM value)
            stmt.setInt(2, bookingId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Make the assigned car available again
                carStmt.setInt(1, bookingId);
                carStmt.executeUpdate();

                // Get driver assigned to this booking
                String driverUsername = getDriverUsernameByBookingId(bookingId);
                if (driverUsername != null && !driverUsername.isEmpty()) {
                    markDriverAsAvailable(driverUsername); // Make driver available again
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelBookingByCustomer(int bookingId) {
        String query = "UPDATE bookings SET status = 'Cancelled' WHERE id = ?";
        String updateDriverQuery = "UPDATE users SET status = 'Available' WHERE username = (SELECT driver_username FROM bookings WHERE id = ?)";
        String updateCarQuery = "UPDATE cars SET availability = 1 WHERE id = (SELECT car_id FROM bookings WHERE id = ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             PreparedStatement updateDriverStmt = conn.prepareStatement(updateDriverQuery);
             PreparedStatement updateCarStmt = conn.prepareStatement(updateCarQuery)) {

            stmt.setInt(1, bookingId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                updateDriverStmt.setInt(1, bookingId);
                updateDriverStmt.executeUpdate();

                updateCarStmt.setInt(1, bookingId);
                updateCarStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




    public void markDriverAsAvailable(String driverUsername) {
        String query = "UPDATE users SET status = 'Available' WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, driverUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Update Booking Status (For Payment & Cancellation)
    public boolean updateBookingStatus(int bookingId, double totalAmount) {
        String query = "UPDATE bookings SET status = 'Completed', estimated_bill = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, totalAmount);
            stmt.setInt(2, bookingId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // ✅ Calculate Discount for Repeat Customers
    public double calculateDiscount(String customerUsername, String driverUsername) {
        String query = "SELECT COUNT(*) FROM bookings WHERE customer_username = ? AND driver_username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerUsername);
            stmt.setString(2, driverUsername);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 1) {
                return 0.10; // 10% discount if booked the same driver before
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // No discount
    }

    // ✅ Get Booking by ID (Used in Payment Processing)
    public Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM bookings WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_username"),
                        rs.getInt("car_id"),
                        rs.getString("driver_username"),
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("status"),
                        rs.getDouble("estimated_bill"),
                        rs.getDouble("distance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasPreviousBookingWithSameDriver(String customerUsername, String driverUsername) {
        String query = "SELECT COUNT(*) FROM bookings WHERE customer_username = ? AND driver_username = ? AND status = 'Completed'";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerUsername);
            stmt.setString(2, driverUsername);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, customer has booked this driver before
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings"; // Get all bookings (all statuses)

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_username"),
                        rs.getInt("car_id"),
                        rs.getString("driver_username"),
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("status"),
                        rs.getDouble("estimated_bill"),
                        rs.getDouble("distance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean cancelAdminBooking(int bookingId) {
        String query = "DELETE FROM bookings WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;  // Return true if booking was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
