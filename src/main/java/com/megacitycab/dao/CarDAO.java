package com.megacitycab.dao;

import com.megacitycab.models.Car;
import com.megacitycab.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private Connection conn;

    public CarDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all cars
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getString("car_model"),
                        rs.getString("car_number"),
                        rs.getString("car_type"),
                        rs.getBoolean("availability"),
                        rs.getString("image"),
                        rs.getDouble("fare_per_km")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    // Get a specific car by ID
    public Car getCarById(int carId) {
        String query = "SELECT * FROM cars WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Car(
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getString("car_model"),
                        rs.getString("car_number"),
                        rs.getString("car_type"),
                        rs.getBoolean("availability"),
                        rs.getString("image"),
                        rs.getDouble("fare_per_km")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new car
    public boolean addCar(Car car) {
        String query = "INSERT INTO cars (car_name, car_model, car_number, car_type, availability, image, fare_per_km) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarModel());
            stmt.setString(3, car.getCarNumber());
            stmt.setString(4, car.getCarType());
            stmt.setBoolean(5, car.isAvailable());
            stmt.setString(6, car.getImage());
            stmt.setDouble(7, car.getFarePerKm());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update a car
    public boolean updateCar(Car car) {
        String query = "UPDATE cars SET car_name=?, car_model=?, car_type=?, availability=?, image=?, fare_per_km=? WHERE car_number=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarModel());
            stmt.setString(3, car.getCarType());
            stmt.setBoolean(4, car.isAvailable());
            stmt.setString(5, car.getImage());
            stmt.setDouble(6, car.getFarePerKm());
            stmt.setString(7, car.getCarNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a car
    public boolean deleteCar(String carNumber) {
        String query = "DELETE FROM cars WHERE car_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get only available cars
    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        String query = "SELECT * FROM cars WHERE availability = 1";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                availableCars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getString("car_model"),
                        rs.getString("car_number"),
                        rs.getString("car_type"),
                        rs.getBoolean("availability"),
                        rs.getString("image"),
                        rs.getDouble("fare_per_km")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableCars;
    }

    // Get fare per km for a car
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

    // Update car availability (0 = unavailable, 1 = available)
    public void setCarAvailability(int carId, boolean available) {
        String query = "UPDATE cars SET availability = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, available ? 1 : 0);
            stmt.setInt(2, carId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
